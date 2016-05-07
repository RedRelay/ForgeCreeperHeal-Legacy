package fr.eyzox.forgecreeperheal.blockdata.multi.selector;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class DoorMultiSelector implements IMultiSelector {

	@Override
	public BlockPos[] getBlockPos(World w, BlockPos pos, IBlockState state) {
		return new BlockPos[]{pos.up()};
	}

}
