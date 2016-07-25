package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.SingleDependency;
import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.util.BlockPos;

public class LeverDependencyBuilder implements IDependencyBuilder{

	@Override
	public boolean accept(Block in) {
		return BlockLever.class.isAssignableFrom(in.getClass());
	}

	@Override
	public DependencyType<BlockPos, BlockData> getDependencies(BlockData data) {
		final EnumOrientation orientation = (EnumOrientation)data.getState().getValue(BlockLever.FACING);
		return new SingleDependency<BlockPos, BlockData>(FacingDependencyUtils.getBlockPos(data.getPos(), orientation.getFacing()));
	}

	

}
