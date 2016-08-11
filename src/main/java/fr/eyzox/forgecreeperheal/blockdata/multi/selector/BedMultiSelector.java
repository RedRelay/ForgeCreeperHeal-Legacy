package fr.eyzox.forgecreeperheal.blockdata.multi.selector;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BedMultiSelector implements IMultiSelector {

	@Override
	public BlockPos[] getBlockPos(World w, BlockPos pos, IBlockState state) {
		return new BlockPos[]{pos.offset(((EnumFacing) state.getValue(BlockBed.FACING)).getOpposite())};
	}

}
