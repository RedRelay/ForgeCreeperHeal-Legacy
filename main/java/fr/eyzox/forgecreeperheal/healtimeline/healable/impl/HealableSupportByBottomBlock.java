package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableSupportByBottomBlock extends HealableBlock {

	public HealableSupportByBottomBlock(World world, BlockPos pos,IBlockState state) {
		super(world, pos, state);
	}

	@Override
	public Object[] getDependencies() {
		return new BlockPos[]{HealableFacingBlock.getRequiredBlockPos(this, EnumFacing.DOWN)};
	}

}
