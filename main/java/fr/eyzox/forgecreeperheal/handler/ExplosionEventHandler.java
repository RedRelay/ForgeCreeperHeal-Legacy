package fr.eyzox.forgecreeperheal.handler;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.factory.DefaultFactory;
import fr.eyzox.forgecreeperheal.healer.WorldRemover;
import fr.eyzox.forgecreeperheal.reflection.Reflect;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExplosionEventHandler implements IEventHandler{

	private Field exploder;

	public ExplosionEventHandler() {
		exploder = Reflect.getFieldForClass(Explosion.class, "exploder", "field_77283_e");
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDetonate(ExplosionEvent.Detonate event) {
		
		if(event.getWorld().isRemote)
			return;
		
		final WorldServer world = (WorldServer) event.getWorld();
		
		//TODO FromPlayer exception config
		
		
		//Entity Filter : Retrieve which entity make this explosion
		Entity exploder = (Entity) Reflect.getDataFromField(this.exploder, event.getExplosion());
		//TODO maybe better config
		if(exploder == null || ForgeCreeperHeal.getConfig().getSourceException().contains(exploder.getClass()))
			return;
		
		//All filters applied, now begins the real stuff :P
		
		final Collection<IBlockData> healables = this.buildBlockDataCollection(world, event.getAffectedBlocks());
		ForgeCreeperHeal.getHealerFactory().create(world, healables);
		//Remove future healed block from world to destroy drop appearing after the explosion and avoid item duplication
		final WorldRemover remover = new WorldRemover(world);
		remover.process(healables);
		
		
		
	}
	
	private Collection<IBlockData> buildBlockDataCollection(WorldServer world, Collection<BlockPos> affectedBlocks) {
		
		final DefaultFactory<Class<? extends Block>, IBlockDataBuilder> blockDataFactory = ForgeCreeperHeal.getBlockDataFactory();
		
		final Collection<IBlockData> healables = new LinkedList<IBlockData>();
		for(final BlockPos pos : affectedBlocks) {
			final IBlockState blockstate = world.getBlockState(pos);
			
			final IBlockDataBuilder builder = blockDataFactory.getData(blockstate.getBlock().getClass());
			final IBlockData data = builder.create(world, pos, blockstate);
			
			if(data != null) {
				healables.add(data);
			}
			
		}
		return healables;
	}

	private Entity getExploderFromExplosion(Explosion explosion) {
		Entity entity = null;
		try {
			entity = (Entity)exploder.get(explosion);
		} catch (ReflectiveOperationException e) {
			CrashReport crash = new CrashReport(e.getLocalizedMessage(), e);
			FMLCommonHandler.instance().enhanceCrashReport(crash, crash.makeCategory(ForgeCreeperHeal.MODNAME));
		}
		return entity;
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

}
