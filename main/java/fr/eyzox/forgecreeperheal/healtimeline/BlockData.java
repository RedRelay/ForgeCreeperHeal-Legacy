package fr.eyzox.forgecreeperheal.healtimeline;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class BlockData {
	private IBlockState blockState;
	private NBTTagCompound tileEntityTag;

	public BlockData(IBlockState blockState, TileEntity te) {
		this.blockState = blockState;
		if(te != null) {
			this.tileEntityTag = new NBTTagCompound();
			te.writeToNBT(tileEntityTag);
		}
	}
	
	public BlockData(IBlockState blockState) {
		this(blockState, null);
	}

	public BlockData() {}

	public IBlockState getBlockState() {
		return blockState;
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

}
