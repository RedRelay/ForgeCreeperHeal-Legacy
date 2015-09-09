package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fluids.FluidRegistry;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.Profiler;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.forgecreeperheal.healtimeline.DefaultBlockDataFactory;
import fr.eyzox.forgecreeperheal.healtimeline.HealTimeline;
import fr.eyzox.forgecreeperheal.healtimeline.IBlockDataFactory;
import fr.eyzox.timeline.Key;

public class WorldHealer extends WorldSavedData{

	private World world;
	private List<HealTimeline> healTimelines = new LinkedList<HealTimeline>();
	
	private static List<IBlockDataFactory> blockDataFactories = new LinkedList<IBlockDataFactory>();
	private static final IBlockDataFactory DEFAULT_BLOCKDATA_FACTORY = new DefaultBlockDataFactory();

	private Profiler profiler;


	public WorldHealer() {
		this(getDataStorageKey());
	}

	public WorldHealer(String key) {
		super(key);
	}

	public void onTick() {
		if(profiler != null) {
			profiler.begin();
			profiler.tickStart();
		}
		
		Iterator<HealTimeline> healGraphIterator = healTimelines.iterator();
		while(healGraphIterator.hasNext()) {
			HealTimeline healTimeline = healGraphIterator.next();
			healTimeline.onTick();
			if(healTimeline.isEmpty()) {
				healGraphIterator.remove();
			}
		}
		

		if(profiler != null) {
			profiler.tickStop();
			//profiler.handleMemoryUse(healTask.getLinkedList());
			profiler.report();
		}
	}

	public void onDetonate(ExplosionEvent.Detonate event) {

		if(profiler != null) {
			profiler.explosionStart();
		}

		if(event.getAffectedBlocks().isEmpty()) return;

		Collection<Key<BlockPos,BlockData>> affectedBlock = getAffectedBlock(event.world, event.getAffectedBlocks());
		
		healTimelines.add(new HealTimeline(this,affectedBlock));
		
		for(Key<BlockPos,BlockData> data : affectedBlock) {
			event.world.setBlockState(data.getKey(), Blocks.air.getDefaultState(), 2);
		}
		
		if(profiler != null) {
			profiler.explosionStop();
		}

	}

	private Collection<Key<BlockPos,BlockData>> getAffectedBlock(World world, Collection<BlockPos> affectedBlocks) {
		HashSet<Key<BlockPos,BlockData>> affectedBlockData = new HashSet<Key<BlockPos,BlockData>>();
		for(BlockPos pos : affectedBlocks) {
			IBlockState blockstate = world.getBlockState(pos);
			
			Iterator<IBlockDataFactory> it = blockDataFactories.iterator();
			BlockData data = null;
			while(data == null && it.hasNext()) {
				IBlockDataFactory factory = it.next();
				if(factory.accept(world, pos, blockstate)) {
					data = factory.createBlockData(world, pos, blockstate);
				}
			}
			
			if(data == null && DEFAULT_BLOCKDATA_FACTORY.accept(world, pos, blockstate)) {
				data = DEFAULT_BLOCKDATA_FACTORY.createBlockData(world, pos, blockstate);
			}
			
			if(data != null) {
				affectedBlockData.add(new Key<BlockPos,BlockData>(pos, data));
			}
		}
		return affectedBlockData;
	}
	
	public void heal(Key<BlockPos,BlockData> data) {
		heal(data, 7);
	}
	
	public void heal(Key<BlockPos,BlockData> data, int flag) {

		boolean isAir = this.world.isAirBlock(data.getKey());

		if(ForgeCreeperHeal.getConfig().isOverride() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(this.world.getBlockState(data.getKey()).getBlock()) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
				world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, data.getKey(), new ItemStack(world.getBlockState(data.getKey()).getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));

				TileEntity te = world.getTileEntity(data.getKey());
				if(te instanceof IInventory) {
					WorldHealerUtils.dropInventory(world, data.getKey(), (IInventory) te);
				}
			}

			world.setBlockState(data.getKey(), data.getValue().getBlockState(), flag);

			if(data.getValue().getTileEntityTag() != null) {
				TileEntity te = world.getTileEntity(data.getKey());
				if(te != null) {
					te.readFromNBT(data.getValue().getTileEntityTag());
					world.setTileEntity(data.getKey(), te);
				}
			}

		}else if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock()){
			world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, data.getKey(), new ItemStack(data.getValue().getBlockState().getBlock()),world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));
			if(data.getValue().getTileEntityTag() != null) {
				TileEntity te = data.getValue().getBlockState().getBlock().createTileEntity(world, data.getValue().getBlockState());
				if(te instanceof IInventory) {
					te.readFromNBT(data.getValue().getTileEntityTag());
					WorldHealerUtils.dropInventory(world, data.getKey(), (IInventory) te);
				}
			}

		}
	}

	public void healAll() {
		
		for(HealTimeline healTimeline : healTimelines) {
			for(Key<BlockPos,BlockData> data : healTimeline) {
				this.heal(data, 2);
			}
		}
		
		healTimelines.clear();
	}


	public World getWorld() {
		return world;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		/*
		NBTTagList tagList = (NBTTagList) tag.getTag("healtasklist");
		for(int i=0; i<tagList.tagCount(); i++) {
			NBTTagCompound tickContainerTag = tagList.getCompoundTagAt(i);
			int ticksLeft = tickContainerTag.getInteger("ticks");
			LinkedList<BlockData> blockDataList = new LinkedList<BlockData>();
			NBTTagList blockDataListTag = (NBTTagList) tickContainerTag.getTag("blockdatalist");
			for(int j=0;j<blockDataListTag.tagCount(); j++) {
				NBTTagCompound blockDataTag = blockDataListTag.getCompoundTagAt(j);
				BlockData blockData = new BlockData();
				blockData.readFromNBT(blockDataTag);
				blockDataList.add(blockData);
			}
			healTask.getLinkedList().addLast(new TickContainer<Collection<BlockData>>(ticksLeft, blockDataList));
		}
		*/

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		/*
		NBTTagList tagList = new NBTTagList();
		for(TickContainer<Collection<BlockData>> tc : this.healTask.getLinkedList()) {
			NBTTagCompound tickContainerTag = new NBTTagCompound();
			tickContainerTag.setInteger("ticks", tc.getTick());
			NBTTagList blockDataListTag = new NBTTagList();
			for(BlockData blockData : tc.getData()) {
				NBTTagCompound blockDataTag = new NBTTagCompound();
				blockData.writeToNBT(blockDataTag);
				blockDataListTag.appendTag(blockDataTag);
			}
			tickContainerTag.setTag("blockdatalist", blockDataListTag);
			tagList.appendTag(tickContainerTag);
		}
		tag.setTag("healtasklist", tagList);
		*/
	}

	public static WorldHealer loadWorldHealer(World w) {
		MapStorage storage = w.getPerWorldStorage();
		final String KEY = getDataStorageKey();
		WorldHealer result = (WorldHealer)storage.loadData(WorldHealer.class, KEY);
		if (result == null) {
			result = new WorldHealer(KEY);
			storage.setData(KEY, result);
			ForgeCreeperHeal.getLogger().info("Unable to find data for world "+w.getWorldInfo().getWorldName()+"["+w.provider.getDimensionId()+"], new data created");
		}

		result.world = w;

		return result;
	}

	public static String getDataStorageKey() {
		return ForgeCreeperHeal.MODID+":"+WorldHealer.class.getSimpleName();
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	public void enableProfiler(ICommandSender sender) {
		if(profiler == null) this.profiler = new Profiler(this);
		profiler.addListener(sender);
	}

	public void disableProfiler(ICommandSender sender) {
		if(profiler != null){
			profiler.removeListener(sender);
		}
	}

	public void disableProfiler() {
		this.profiler = null;
	}

	public boolean isProfilerEnabled() {
		return this.profiler != null;
	}

	public Profiler getProfiler() {
		return profiler;
	}

}
