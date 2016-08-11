package fr.eyzox.forgecreeperheal.blockdata.multi.selector;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiSelector {
	public BlockPos[] getBlockPos(World w, BlockPos pos, IBlockState state);
}
