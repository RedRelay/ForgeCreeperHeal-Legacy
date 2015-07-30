package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fluids.FluidRegistry;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.Profiler;
import fr.eyzox.ticklinkedlist.TickContainer;

public class WorldHealer extends WorldSavedData{

	private World world;
	private HealTask healTask;

	private Profiler profiler;

	public WorldHealer() {
		this(getDataStorageKey());
	}

	public WorldHealer(String key) {
		super(key);
		healTask = new HealTask();
	}

	public void onTick() {
		if(profiler != null) {
			profiler.begin();
			profiler.tickStart();
		}

		Collection<BlockData> blocksToHeal = healTask.tick();
		if(blocksToHeal != null) {
			for(BlockData blockData : blocksToHeal) {
				heal(blockData);
			}
		}

		if(profiler != null) {
			profiler.tickStop();
			profiler.handleMemoryUse(healTask.getLinkedList());
			profiler.report();
		}
	}

	public void onDetonate(ExplosionEvent.Detonate event) {
		if(profiler != null) {
			profiler.explosionStart();
		}
		int maxTicksBeforeHeal = 0;
		//Process primary blocks
		for(BlockPos blockPosExplosion : event.getAffectedBlocks()) {
			IBlockState blockStateExplosion = event.world.getBlockState(blockPosExplosion);
			if(blockStateExplosion.getBlock().isNormalCube()) {

				int ticksBeforeHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal()+world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar());
				if(ticksBeforeHeal > maxTicksBeforeHeal) {
					maxTicksBeforeHeal = ticksBeforeHeal;
				}

				onBlockHealed(blockPosExplosion, blockStateExplosion, ticksBeforeHeal);
			}
		}
		maxTicksBeforeHeal++;

		//Process secondary blocks
		for(BlockPos blockPosExplosion : event.getAffectedBlocks()) {
			IBlockState blockStateExplosion = event.world.getBlockState(blockPosExplosion);
			if(!blockStateExplosion.getBlock().isNormalCube() && !blockStateExplosion.getBlock().isAir(event.world, blockPosExplosion)) {
				onBlockHealed(blockPosExplosion, blockStateExplosion, maxTicksBeforeHeal + world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar()));
			}
		}
		if(profiler != null) {
			profiler.explosionStop();
		}
	}

	private void onBlockHealed(BlockPos blockPosExplosion, IBlockState blockStateExplosion, int ticks) {
		if(ForgeCreeperHeal.getConfig().isDropItemsFromContainer() && !ForgeCreeperHeal.getConfig().getRemoveException().contains(blockStateExplosion.getBlock())) {
			TileEntity te = world.getTileEntity(blockPosExplosion);
			if(te instanceof IInventory) {
				WorldHealerUtils.dropInventory(world, blockPosExplosion, (IInventory) te);
			}
		}

		if(!ForgeCreeperHeal.getConfig().getHealException().contains(blockStateExplosion.getBlock())) {
			healTask.add(ticks, new BlockData(world,blockPosExplosion, blockStateExplosion));
		}

		if(!ForgeCreeperHeal.getConfig().getRemoveException().contains(blockStateExplosion.getBlock())) {
			world.removeTileEntity(blockPosExplosion);
			world.setBlockState(blockPosExplosion, Blocks.air.getDefaultState(), 7);
		}
	}

	private void heal(BlockData blockData) {
		boolean isAir = this.world.isAirBlock(blockData.getBlockPos());

		if(ForgeCreeperHeal.getConfig().isOverride() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(this.world.getBlockState(blockData.getBlockPos()).getBlock()) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
				world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(), new ItemStack(world.getBlockState(blockData.getBlockPos()).getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));

				TileEntity te = world.getTileEntity(blockData.getBlockPos());
				if(te instanceof IInventory) {
					WorldHealerUtils.dropInventory(world, blockData.getBlockPos(), (IInventory) te);
				}
			}

			world.setBlockState(blockData.getBlockPos(), blockData.getBlockState(), 7);

			if(blockData.getTileEntityTag() != null) {
				TileEntity te = world.getTileEntity(blockData.getBlockPos());
				if(te != null) {
					te.readFromNBT(blockData.getTileEntityTag());
					world.setTileEntity(blockData.getBlockPos(), te);
				}
			}

		}else if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock()){
			world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(), new ItemStack(blockData.getBlockState().getBlock()),world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));
			if(blockData.getTileEntityTag() != null) {
				TileEntity te = blockData.getBlockState().getBlock().createTileEntity(world, blockData.getBlockState());
				if(te instanceof IInventory) {
					te.readFromNBT(blockData.getTileEntityTag());
					WorldHealerUtils.dropInventory(world, blockData.getBlockPos(), (IInventory) te);
				}
			}

		}
	}


	public World getWorld() {
		return world;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
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

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
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
