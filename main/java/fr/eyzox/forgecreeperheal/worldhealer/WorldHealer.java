package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
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
	private HealTask toHeal;

	private Profiler profiler;

	public WorldHealer() {
		this(getDataStorageKey());
	}

	public WorldHealer(String key) {
		super(key);
		toHeal = new HealTask();
	}

	public void onTick() {
		if(profiler != null) {
			profiler.begin();
			profiler.tickStart();
		}

		Collection<BlockData> blocksToHeal = toHeal.tick();
		if(blocksToHeal != null) {
			for(BlockData blockData : blocksToHeal) {
				heal(blockData);
			}
		}

		if(profiler != null) {
			profiler.tickStop();
			profiler.handleMemoryUse(toHeal.getLinkedList());
			profiler.report();
		}
	}

	public void onDetonate(ExplosionEvent.Detonate event) {
		if(profiler != null) {
			profiler.explosionStart();
		}
		int maxTicksBeforeHeal = 0;
		//Process primary blocks
		for(ChunkPosition c : event.getAffectedBlocks()) {
			Block block = event.world.getBlock(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
			if(block.isNormalCube()) {

				int ticksBeforeHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal()+world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar());
				if(ticksBeforeHeal > maxTicksBeforeHeal) {
					maxTicksBeforeHeal = ticksBeforeHeal;
				}

				onBlockHealed(c, block, ticksBeforeHeal);
			}
		}
		maxTicksBeforeHeal++;

		//Process secondary blocks
		for(ChunkPosition c : event.getAffectedBlocks()) {
			Block block = event.world.getBlock(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
			if(!block.isNormalCube() && !block.isAir(event.world, c.chunkPosX, c.chunkPosY, c.chunkPosZ)) {
				onBlockHealed(c, block, maxTicksBeforeHeal + world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar()));
			}
		}
		if(profiler != null) {
			profiler.explosionStop();
		}
	}

	private void onBlockHealed(ChunkPosition c, Block block, int ticks) {
		if(ForgeCreeperHeal.getConfig().isDropItemsFromContainer() && !ForgeCreeperHeal.getConfig().getRemoveException().contains(block)) {
			TileEntity te = world.getTileEntity(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
			if(te instanceof IInventory) {
				WorldHealerUtils.dropInventory(world, c, (IInventory) te);
			}
		}

		if(!ForgeCreeperHeal.getConfig().getHealException().contains(block)) {
			toHeal.add(ticks, new BlockData(world,c));
		}

		if(!ForgeCreeperHeal.getConfig().getRemoveException().contains(block)) {
			world.removeTileEntity(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
			world.setBlock(c.chunkPosX, c.chunkPosY, c.chunkPosZ, Blocks.air, 0, 7);
		}
	}

	private void heal(BlockData blockData) {
		boolean isAir = this.world.isAirBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ);

		if(ForgeCreeperHeal.getConfig().isOverride() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(this.world.getBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ)) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
				world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, blockData.getChunkPosition(), new ItemStack(world.getBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ)), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));

				TileEntity te = world.getTileEntity(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ);
				if(te instanceof IInventory) {
					WorldHealerUtils.dropInventory(world, blockData.getChunkPosition(), (IInventory) te);
				}
			}

			world.setBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ, blockData.getBlock(), blockData.getMetadata(), 7);
			world.setBlockMetadataWithNotify(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ, blockData.getMetadata(), 7);
			if(blockData.getTileEntityTag() != null) {
				TileEntity te = world.getTileEntity(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ);
				if(te != null) {
					te.readFromNBT(blockData.getTileEntityTag());
					world.setTileEntity(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ, te);
				}
			}

		}else if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock()){
			world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, blockData.getChunkPosition(), new ItemStack(blockData.getBlock()),world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));
			if(blockData.getTileEntityTag() != null) {
				TileEntity te = blockData.getBlock().createTileEntity(world, blockData.getMetadata());
				if(te instanceof IInventory) {
					te.readFromNBT(blockData.getTileEntityTag());
					WorldHealerUtils.dropInventory(world, blockData.getChunkPosition(), (IInventory) te);
				}
			}

		}
	}


	public World getWorld() {
		return world;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList tagList = (NBTTagList) tag.getTag("toHeal");
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
			toHeal.getLinkedList().addLast(new TickContainer<Collection<BlockData>>(ticksLeft, blockDataList));
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList tagList = new NBTTagList();
		for(TickContainer<Collection<BlockData>> tc : this.toHeal.getLinkedList()) {
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
		tag.setTag("toHeal", tagList);
	}

	public static WorldHealer loadWorldHealer(World w) {
		MapStorage storage = w.perWorldStorage;
		final String KEY = getDataStorageKey();
		WorldHealer result = (WorldHealer)storage.loadData(WorldHealer.class, KEY);
		if (result == null) {
			result = new WorldHealer(KEY);
			storage.setData(KEY, result);
			ForgeCreeperHeal.getLogger().info("Unable to find data for world "+w.getWorldInfo().getWorldName()+"["+w.provider.dimensionId+"], new data created");
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
