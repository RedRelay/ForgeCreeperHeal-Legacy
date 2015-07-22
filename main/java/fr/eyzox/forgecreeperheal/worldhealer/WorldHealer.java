package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockClay;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockWood;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fluids.FluidRegistry;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class WorldHealer extends WorldSavedData{

	private Random rdn = new Random();
	private World world;
	private LinkedList<TickContainer> toHeal;

	public WorldHealer() {
		this(getDataStorageKey());
	}
	
	public WorldHealer(String key) {
		super(key);
		toHeal = new LinkedList<TickContainer>();
	}

	public void add(BlockData blockData, int ticks) {
		if(toHeal.isEmpty()) {
			toHeal.add(new TickContainer(ticks, blockData));
		}else {
			for(ListIterator<TickContainer> it = toHeal.listIterator(); it.hasNext();){
				TickContainer current = it.next();
				if(current.getTicksLeft() < ticks) {
					ticks = ticks - current.getTicksLeft();
				}else if(current.getTicksLeft() == ticks) {
					current.getBlockDataList().add(blockData);
					return;
				}else {
					it.set(new TickContainer(ticks, blockData));
					current.setTicksLeft(current.getTicksLeft()-ticks);
					it.add(current);
					return;
				}
			}

			toHeal.add(new TickContainer(ticks, blockData));
		}
	}

	public void onTick() {
		if(!toHeal.isEmpty()) {
			TickContainer tc = toHeal.getFirst();

			if(tc.getTicksLeft() <= 1) {
				toHeal.removeFirst();
				for(BlockData blockData : tc.getBlockDataList()) {
					heal(blockData);
				}
			}else {
				tc.setTicksLeft(tc.getTicksLeft() - 1);
			}
		}
	}

	private void heal(BlockData blockData) {
		boolean isAir = this.world.isAirBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ);

		if(ForgeCreeperHeal.getConfig().isOverride() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(this.world.getBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ)) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
				world.spawnEntityInWorld(buildEntityItem(blockData.getChunkPosition(), new ItemStack(world.getBlock(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ)), rdn.nextFloat() * 0.8F + 0.1F, rdn.nextFloat() * 0.8F + 0.1F, rdn.nextFloat() * 0.8F + 0.1F, 0.05F));

				TileEntity te = world.getTileEntity(blockData.getChunkPosition().chunkPosX, blockData.getChunkPosition().chunkPosY, blockData.getChunkPosition().chunkPosZ);
				if(te instanceof IInventory) {
					dropInventory((IInventory) te, blockData.getChunkPosition());
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
			world.spawnEntityInWorld(buildEntityItem(blockData.getChunkPosition(), new ItemStack(blockData.getBlock()),rdn.nextFloat() * 0.8F + 0.1F, rdn.nextFloat() * 0.8F + 0.1F, rdn.nextFloat() * 0.8F + 0.1F, 0.05F));
			if(blockData.getTileEntityTag() != null) {
				TileEntity te = blockData.getBlock().createTileEntity(world, blockData.getMetadata());
				if(te instanceof IInventory) {
					te.readFromNBT(blockData.getTileEntityTag());
					dropInventory((IInventory) te, blockData.getChunkPosition());
				}
			}

		}
	}


	private EntityItem buildEntityItem(ChunkPosition cp, ItemStack itemStack, float deltaX, float deltaY, float deltaZ, float motion) {
		EntityItem entityitem = new EntityItem(world, (double)((float)cp.chunkPosX + deltaX), (double)((float)cp.chunkPosY + deltaY), (double)((float)cp.chunkPosZ + deltaZ), itemStack);
		entityitem.motionX = (double)((float)rdn.nextGaussian() * motion);
		entityitem.motionY = (double)((float)rdn.nextGaussian() * motion + 0.2F);
		entityitem.motionZ = (double)((float)rdn.nextGaussian() * motion);
		return entityitem;
	}

	public void dropInventory(IInventory inventory, ChunkPosition cp) {
		for (int inventoryIndex = 0; inventoryIndex < inventory.getSizeInventory(); ++inventoryIndex)
		{
			ItemStack itemstack = inventory.getStackInSlot(inventoryIndex);

			if (itemstack != null)
			{

				float f = rdn.nextFloat() * 0.8F + 0.1F;
				float f1 = rdn.nextFloat() * 0.8F + 0.1F;

				for (float f2 = rdn.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; )
				{
					int j1 = rdn.nextInt(21) + 10;

					if (j1 > itemstack.stackSize)
					{
						j1 = itemstack.stackSize;
					}

					itemstack.stackSize -= j1;

					float f3 = 0.05F;
					EntityItem entityitem = buildEntityItem(cp, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()), f, f1, f2, f3);
					


					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
					
					world.spawnEntityInWorld(entityitem);
				}

				/*ADDED TO REMOVE ITEMSTACK FROM INVENTORY*/
				inventory.setInventorySlotContents(inventoryIndex, null);
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
			toHeal.add(new TickContainer(ticksLeft, blockDataList));
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList tagList = new NBTTagList();
		for(TickContainer tc : this.toHeal) {
			NBTTagCompound tickContainerTag = new NBTTagCompound();
			tickContainerTag.setInteger("ticks", tc.getTicksLeft());
			NBTTagList blockDataListTag = new NBTTagList();
			for(BlockData blockData : tc.getBlockDataList()) {
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
	
	

}
