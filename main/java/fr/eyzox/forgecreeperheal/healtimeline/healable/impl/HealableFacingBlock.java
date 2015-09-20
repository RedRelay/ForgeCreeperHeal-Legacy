package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableFacingBlock extends HealableBlock {
	
	private final PropertyDirection facingProperty;
	

	public HealableFacingBlock(PropertyDirection facingProperty, World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		this.facingProperty = facingProperty;
	}

	
	protected EnumFacing getEnumFacing() {
		return (EnumFacing) this.getBlockState().getValue(facingProperty);
	}
	
	@Override
	public Object[] getDependencies() {
		return new BlockPos[] {getRequiredBlockPos(this.getPos(), getEnumFacing().getOpposite())};
	}

	public static BlockPos getRequiredBlockPos(BlockPos pos, EnumFacing e) {
		return pos.offset(e);
	}

	
}