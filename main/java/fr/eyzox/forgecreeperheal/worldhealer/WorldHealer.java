package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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

	private ArrayList<Block> blacklist = new ArrayList<Block>();
			
	public WorldHealer() {
		this(getDataStorageKey());
		//for simplicity , just ignore any 2 size blocks (beds)
		blacklist.add(null);
		blacklist.add(Blocks.AIR);
		blacklist.add(Blocks.BED);
		blacklist.add(Blocks.ACACIA_DOOR);
		blacklist.add(Blocks.JUNGLE_DOOR);
		blacklist.add(Blocks.BIRCH_DOOR);
		blacklist.add(Blocks.OAK_DOOR);
		blacklist.add(Blocks.DARK_OAK_DOOR);
	}

	public WorldHealer(String key) {
		super(key);
		healTask = new HealTask();
	}

	public World getWorld() {
		return world;
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
		World world = event.getWorld();
		int maxTicksBeforeHeal = 0;
		//Process primary blocks
		for(BlockPos blockPosExplosion : event.getAffectedBlocks()) {
			IBlockState blockStateExplosion = world.getBlockState(blockPosExplosion);
			if(blacklist.contains(blockStateExplosion.getBlock())){continue;}
			if(blockStateExplosion.getBlock().isNormalCube(blockStateExplosion,world,blockPosExplosion)) {

				int ticksBeforeHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal() + world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar());
				if(ticksBeforeHeal > maxTicksBeforeHeal) {
					maxTicksBeforeHeal = ticksBeforeHeal;
				}

				onBlockHealed(blockPosExplosion, blockStateExplosion, ticksBeforeHeal);
			}
		}
		maxTicksBeforeHeal++;

		//Process secondary blocks. ex: Leaves must come AFTER dirt
		for(BlockPos blockPosExplosion : event.getAffectedBlocks()) {
			IBlockState blockStateExplosion = world.getBlockState(blockPosExplosion);
			if(blacklist.contains(blockStateExplosion.getBlock())){continue;}
			if(!blockStateExplosion.getBlock().isNormalCube(blockStateExplosion,world,blockPosExplosion)) {//.isAir(world, blockPosExplosion)

				onBlockHealed(blockPosExplosion, blockStateExplosion, maxTicksBeforeHeal + world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar()));
			}
		}
		if(profiler != null) {
			profiler.explosionStop();
		}
	}

	private void onBlockHealed(BlockPos blockPosExplosion, IBlockState blockStateExplosion, int ticks) {
		healTask.add(ticks, new BlockData(world,blockPosExplosion, blockStateExplosion));
		world.removeTileEntity(blockPosExplosion);
		world.setBlockState(blockPosExplosion, Blocks.AIR.getDefaultState(), 7);
	}

	private void heal(BlockData blockData) {
		boolean isAir = this.world.isAirBlock(blockData.getBlockPos());

		if(ForgeCreeperHeal.getConfig().isOverride() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(this.world.getBlockState(blockData.getBlockPos()).getBlock()) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
				
				if(world.getBlockState(blockData.getBlockPos()).getBlock() != null){
					EntityItem ei = WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(), new ItemStack(world.getBlockState(blockData.getBlockPos()).getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F);
					
					if(ei != null){
						world.spawnEntityInWorld(ei);
						TileEntity te = world.getTileEntity(blockData.getBlockPos());
						if(te instanceof IInventory) {
							WorldHealerUtils.dropInventory(world, blockData.getBlockPos(), (IInventory) te);
						}
					}
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

		}else if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && blockData.getBlockState().getBlock() != null){
 
			EntityItem ei = WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(), new ItemStack(blockData.getBlockState().getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F);
			if(ei != null){
				world.spawnEntityInWorld(ei);
				if(blockData.getTileEntityTag() != null) {
					TileEntity te = blockData.getBlockState().getBlock().createTileEntity(world, blockData.getBlockState());
					if(te instanceof IInventory) {
						te.readFromNBT(blockData.getTileEntityTag());
						WorldHealerUtils.dropInventory(world, blockData.getBlockPos(), (IInventory) te);
					}
				}
			}
		}
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
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
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
		return tag;
	}

	public static WorldHealer loadWorldHealer(World w) {
		MapStorage storage = w.getPerWorldStorage();
		final String KEY = getDataStorageKey();
		WorldHealer result = (WorldHealer)storage.getOrLoadData(WorldHealer.class, KEY);
		if (result == null) {
			result = new WorldHealer(KEY);
			storage.setData(KEY, result);
			ForgeCreeperHeal.getLogger().info("Unable to find data for world "+w.getWorldInfo().getWorldName()+"["+w.provider.getDimension()+"], new data created");
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
