package fr.eyzox.forgecreeperheal.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.factory.DefaultFactory;
import fr.eyzox.forgecreeperheal.healer.Healer;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import fr.eyzox.forgecreeperheal.healer.WorldRemover;
import fr.eyzox.forgecreeperheal.reflection.ReflectionHelper;
import fr.eyzox.forgecreeperheal.reflection.ReflectionManager;
import fr.eyzox.forgecreeperheal.scheduler.custom.CustomRandomScheduler;
import fr.eyzox.forgecreeperheal.scheduler.graph.dependency.provider.BlockDataDependencyProvider;
import fr.eyzox.ticktimeline.Node;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExplosionEventHandler implements IEventHandler{

	private static final Field EXPLODER = ReflectionManager.getInstance().getField(Explosion.class, "exploder");


	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDetonate(ExplosionEvent.Detonate event) {
		
		if(event.getWorld().isRemote)
			return;
		
		final WorldServer world = (WorldServer) event.getWorld();
		
		//TODO FromPlayer exception config
		
		
		//Entity Filter : Retrieve which entity make this explosion
		Entity exploder = (Entity) ReflectionHelper.get(ExplosionEventHandler.EXPLODER, event.getExplosion());
		//TODO maybe better config
		if(exploder == null || ForgeCreeperHeal.getConfig().getSourceException().contains(exploder.getClass().getName()))
			return;
		
		//All filters applied, now begins the real stuff :P
		
		final Collection<BlockData> affectedBlockData = this.buildBlockDataCollection(world, event.getAffectedBlocks());
		
		final Collection<BlockData> toHeal = new ArrayList<BlockData>(affectedBlockData.size());
		for(final BlockData data : affectedBlockData) {
			if(!ForgeCreeperHeal.getConfig().getHealException().contains(data.getState().getBlock().getRegistryName().toString())) {
				toHeal.add(data);
			}
		}
		
		//Process TileEntity
		for(final BlockData data : toHeal) {
			if(data.getState().getBlock().hasTileEntity(data.getState())) {
				TileEntity te = world.getTileEntity(data.getPos());
				if(te != null) {
					data.setTileEntity(te);
				}
			}
		}
		
		final Map<ChunkPos, Collection<Node<BlockData>>> addToTimeline = ForgeCreeperHeal.getHealerFactory().create(world, new CustomRandomScheduler<BlockPos, BlockData>(toHeal, BlockDataDependencyProvider.getInstance()));
		
		
		final HealerManager manager = ForgeCreeperHeal.getHealerManager((WorldServer) event.getWorld());
		for(final Entry<ChunkPos, Collection<Node<BlockData>>> entry : addToTimeline.entrySet()) {
			Healer healer = manager.load(entry.getKey());
			if(healer == null) {
				healer = new Healer(event.getWorld().getChunkFromChunkCoords(entry.getKey().chunkXPos, entry.getKey().chunkZPos));
			}
			healer.getTimeline().add(entry.getValue());
			manager.hook(healer);
		}
		
		//Remove future healed block from world to destroy drop appearing after the explosion and avoid item duplication
		final WorldRemover remover = new WorldRemover(world);
		for(BlockData block : affectedBlockData) {
			if(!ForgeCreeperHeal.getConfig().getRemoveException().contains(block.getState().getBlock().getRegistryName().toString())) {
				block.remove(remover);
			}
		}
		remover.update(3);
		
		
		
	}
	
	private Collection<BlockData> buildBlockDataCollection(WorldServer world, Collection<BlockPos> affectedBlocks) {
		
		final DefaultFactory<Block, IBlockDataBuilder> blockDataFactory = ForgeCreeperHeal.getBlockDataFactory();
		
		final Collection<BlockData> healables = new LinkedList<BlockData>();
		for(final BlockPos pos : affectedBlocks) {
			final IBlockState blockstate = world.getBlockState(pos);
			
			final IBlockDataBuilder builder = blockDataFactory.getData(blockstate.getBlock());
			final BlockData data = builder.create(world, pos, blockstate);
			
			if(data != null) {
				healables.add(data);
			}
			
		}
		return healables;
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

}
