package fr.eyzox.forgecreeperheal.healtimeline;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.requirementcheck.IRequirementChecker;

public class BlockData {
	private IBlockState blockState;
	private NBTTagCompound tileEntityTag;
	private BlockPos blockPos;
	private IRequirementChecker requierement;

	public BlockData(World world, BlockPos chunkPosition, IBlockState blockState) {
		this.blockState = blockState;
		this.blockPos = chunkPosition;
		TileEntity te = world.getTileEntity(chunkPosition);
		if(te != null) {
			this.tileEntityTag = new NBTTagCompound();
			te.writeToNBT(tileEntityTag);
		}
	}

	public BlockData() {}

	public IBlockState getBlockState() {
		return blockState;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public NBTTagCompound getTileEntityTag() {
		return tileEntityTag;
	}

	public void readFromNBT(NBTTagCompound tag) {
		/*
		BlockData prevBlockData = null;
		BlockData currentBlockData = this;
		NBTTagCompound cursorTag = tag;
		
		while(!(cursorTag = cursorTag.getCompoundTag("BlockData")).hasNoTags()) {
			Block block = Block.getBlockFromName(cursorTag.getString("block"));
			if(block != null) {
				currentBlockData.blockState = block.getStateFromMeta(cursorTag.getInteger("metadata"));
				currentBlockData.blockPos = BlockPos.fromLong(cursorTag.getLong("coords"));
				currentBlockData.tileEntityTag = cursorTag.getCompoundTag("tileentity");
				if(currentBlockData.tileEntityTag.hasNoTags()) currentBlockData.tileEntityTag = null;
			}
			
			if(prevBlockData != null) {
				prevBlockData.next = currentBlockData;
			}
			
			prevBlockData = currentBlockData;
		}
		*/
	}

	public void writeToNBT(NBTTagCompound tag) {
/*
		BlockData cursorBlockData = this;
		NBTTagCompound cursorTag = tag;

		do {

			NBTTagCompound blockDataTag = new NBTTagCompound();
			cursorTag.setTag("BlockData", blockDataTag);

			blockDataTag.setString("block", GameData.getBlockRegistry().getNameForObject(cursorBlockData.blockState.getBlock()).toString());
			int metadata = cursorBlockData.blockState.getBlock().getMetaFromState(cursorBlockData.blockState);
			if(metadata != 0) {
				blockDataTag.setInteger("metadata", metadata);
			}
			blockDataTag.setLong("coords", cursorBlockData.blockPos.toLong());
			if(cursorBlockData.tileEntityTag != null) {
				blockDataTag.setTag("tileentity", cursorBlockData.tileEntityTag);
			}

			
			cursorTag = blockDataTag;

		}while((cursorBlockData = cursorBlockData.getNext()) != null);*/
	}

	public void setBlockState(IBlockState blockState) {
		this.blockState = blockState;
	}

	public void setTileEntityTag(NBTTagCompound tileEntityTag) {
		this.tileEntityTag = tileEntityTag;
	}

	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	public IRequirementChecker getRequierement() {
		return requierement;
	}

	public void setRequierement(IRequirementChecker requierement) {
		this.requierement = requierement;
	}

}
