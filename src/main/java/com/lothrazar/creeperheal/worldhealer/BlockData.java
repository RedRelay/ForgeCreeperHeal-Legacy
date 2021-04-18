package com.lothrazar.creeperheal.worldhealer;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockData {

  private BlockPos blockPos;
  private BlockState blockState;
  private CompoundNBT tileEntityTag;

  public BlockData(World world, BlockPos chunkPosition, BlockState blockState) {
    this.blockState = blockState;
    this.blockPos = chunkPosition;
    TileEntity te = world.getTileEntity(chunkPosition);
    if (te != null) {
      this.tileEntityTag = new CompoundNBT();
      te.write(tileEntityTag);
    }
  }

  public BlockData() {}

  public BlockState getBlockState() {
    return blockState;
  }

  public BlockPos getBlockPos() {
    return blockPos;
  }

  public CompoundNBT getTileEntityTag() {
    return tileEntityTag;
  }

  public void readFromNBT(CompoundNBT tag) {
    this.blockState = NBTUtil.readBlockState(tag.getCompound("block"));
    this.blockPos = NBTUtil.readBlockPos(tag.getCompound("pos"));
    if (tag.contains("tileentity")) {
      this.tileEntityTag = tag.getCompound("tileentity");
    }
  }

  public void writeToNBT(CompoundNBT tag) {
    CompoundNBT encoded = NBTUtil.writeBlockState(this.blockState);
    tag.put("block", encoded);
    CompoundNBT epos = NBTUtil.writeBlockPos(this.blockPos);
    tag.put("pos", epos);
    if (this.tileEntityTag != null) {
      tag.put("tileentity", this.tileEntityTag);
    }
  }

  public void setBlockState(BlockState blockState) {
    this.blockState = blockState;
  }

  public void setTileEntityTag(CompoundNBT tileEntityTag) {
    this.tileEntityTag = tileEntityTag;
  }

  public void setBlockPos(BlockPos blockPos) {
    this.blockPos = blockPos;
  }
}
