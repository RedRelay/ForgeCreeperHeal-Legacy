package fr.eyzox.forgecreeperheal.worldhealer;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;

public class BlockData {
	private Block block;
	private int metadata;
	private NBTTagCompound tileEntityTag;
	private ChunkPosition chunkPosition;
	
	public BlockData(World world, ChunkPosition chunkPosition) {
		this(world.getBlock(chunkPosition.chunkPosX, chunkPosition.chunkPosY, chunkPosition.chunkPosZ));
		this.chunkPosition = chunkPosition;
		this.metadata = world.getBlockMetadata(chunkPosition.chunkPosX, chunkPosition.chunkPosY, chunkPosition.chunkPosZ);
		TileEntity te = world.getTileEntity(chunkPosition.chunkPosX, chunkPosition.chunkPosY, chunkPosition.chunkPosZ);
		if(te != null) {
			this.tileEntityTag = new NBTTagCompound();
			te.writeToNBT(tileEntityTag);
		}
	}
	
	protected BlockData(Block block) {
		this.block = block;
	}
	
	protected BlockData() {}

	public Block getBlock() {
		return block;
	}

	public int getMetadata() {
		return metadata;
	}

	public ChunkPosition getChunkPosition() {
		return chunkPosition;
	}

	public NBTTagCompound getTileEntityTag() {
		return tileEntityTag;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		this.block = Block.getBlockFromName(tag.getString("block"));
		this.metadata = tag.getInteger("metadata");
		int[] coords = tag.getIntArray("chunkposition");
		this.chunkPosition = new ChunkPosition(coords[0], coords[1], coords[2]);
		this.tileEntityTag = tag.getCompoundTag("tileentity");
		if(this.tileEntityTag.hasNoTags()) this.tileEntityTag = null;
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setString("block", GameData.getBlockRegistry().getNameForObject(this.block));
		if(metadata != 0) {
			tag.setInteger("metadata", this.metadata);
		}
		tag.setIntArray("chunkposition", new int[]{this.chunkPosition.chunkPosX,this.chunkPosition.chunkPosY,this.chunkPosition.chunkPosZ});
		if(this.tileEntityTag != null) {
			tag.setTag("tileentity", this.tileEntityTag);
		}
	}
	
}
