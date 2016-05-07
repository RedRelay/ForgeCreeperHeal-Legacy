package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.blockdata.MultiBlockData;
import fr.eyzox.forgecreeperheal.blockdata.TileEntityBlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.PistonMultiSelector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class PistonBlockDataBuilder extends MultiBlockDataBuilder {

	public PistonBlockDataBuilder() {
		super(null, new PistonMultiSelector());
	}

	@Override
	public boolean accept(Class<? extends Block> clazz) {
		return BlockPistonBase.class.isAssignableFrom(clazz) || BlockPistonExtension.class.isAssignableFrom(clazz) || BlockPistonMoving.class.isAssignableFrom(clazz);
	}

	@Override
	public IBlockData create(World w, BlockPos pos, IBlockState state) {
		final NBTTagCompound tileEntity = BlockDataBuilderUtils.getTileEntityNBT(w, pos, state);
		if(BlockPistonBase.class.isAssignableFrom(state.getBlock().getClass())) {
			if( ((Boolean)state.getValue(BlockPistonBase.EXTENDED)).booleanValue() ) {
				return super.create(w, pos, state, tileEntity);
			}
			return new TileEntityBlockData(pos, state, tileEntity);
		}
		return null;
	}

	@Override
	public IBlockData create(NBTTagCompound tag) {
		if(MultiBlockData.isMultipleBlockData(tag)) {
			return new MultiBlockData(tag);
		}
		return new TileEntityBlockData(tag);
	}



}
