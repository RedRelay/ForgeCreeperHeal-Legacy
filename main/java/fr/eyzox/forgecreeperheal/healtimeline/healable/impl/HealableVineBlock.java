package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableVineBlock extends HealableBlock {

	public HealableVineBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
	}

	@Override
	public Object[] getDependencies() {
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			if(((Boolean)this.getBlockState().getValue(BlockVine.getPropertyFor(facing))).booleanValue()) {
				return new BlockPos[]{HealableFacingBlock.getRequiredBlockPos(this, facing),HealableFacingBlock.getRequiredBlockPos(this,EnumFacing.UP)};
			}
		}
		return super.getDependencies();
	}
	
	

}
