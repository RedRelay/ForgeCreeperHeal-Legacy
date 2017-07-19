package fr.eyzox.forgecreeperheal.worldhealer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.GameData;

public class BlockData {
	private IBlockState blockState;
	private NBTTagCompound tileEntityTag;
	private BlockPos blockPos;
	
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
		Block block = Block.getBlockFromName(tag.getString("block"));
		if(block != null) {
		  //deprecated in 1.10, possibly earlier
			this.blockState = block.getStateFromMeta(tag.getInteger("metadata"));
			this.blockPos = BlockPos.fromLong(tag.getLong("coords"));
			this.tileEntityTag = tag.getCompoundTag("tileentity");
			if(this.tileEntityTag.hasNoTags()) this.tileEntityTag = null;
		}
	}

	public void writeToNBT(NBTTagCompound tag) {

    //deprecated in 1.10, possibly earlier
	
		tag.setString("block",  this.blockState.getBlock().getRegistryName().toString());
		int metadata = blockState.getBlock().getMetaFromState(blockState);
		if(metadata != 0) {
			tag.setInteger("metadata", metadata);
		}
		tag.setLong("coords", blockPos.toLong());
		if(this.tileEntityTag != null) {
			tag.setTag("tileentity", this.tileEntityTag);
		}
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
}
