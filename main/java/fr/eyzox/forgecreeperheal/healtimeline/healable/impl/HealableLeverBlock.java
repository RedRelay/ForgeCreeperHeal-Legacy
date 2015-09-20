package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HealableLeverBlock extends HealableBlock {

	public HealableLeverBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
	}

	@Override
	public Object[] getDependencies() {
		return new BlockPos[] {HealableFacingBlock.getRequiredBlockPos(this.getPos(), ((EnumOrientation)this.getBlockState().getValue(BlockLever.FACING)).getFacing())};
	}
	
	

}
