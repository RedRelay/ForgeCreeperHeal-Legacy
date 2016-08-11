package fr.eyzox.forgecreeperheal.blockdata.multi.selector;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class PistonMultiSelector implements IMultiSelector {

	@Override
	public BlockPos[] getBlockPos(World w, BlockPos pos, IBlockState state) {
		return new BlockPos[]{pos.offset((EnumFacing) state.getValue(BlockPistonBase.FACING))};
	}

}
