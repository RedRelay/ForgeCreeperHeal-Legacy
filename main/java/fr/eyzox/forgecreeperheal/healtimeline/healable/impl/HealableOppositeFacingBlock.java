package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableOppositeFacingBlock extends HealableFacingBlock {

	public HealableOppositeFacingBlock(PropertyDirection facingProperty, World world, BlockPos pos, IBlockState state) {
		super(facingProperty, world, pos, state);
	}

	@Override
	protected EnumFacing getEnumFacing() {
		return super.getEnumFacing().getOpposite();
	}
	
	

}
