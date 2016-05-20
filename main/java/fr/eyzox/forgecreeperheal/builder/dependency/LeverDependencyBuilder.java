package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.IDependency;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.dependency.FullAndDependency;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.util.math.BlockPos;

public class LeverDependencyBuilder implements IDependencyBuilder{

	@Override
	public boolean accept(Class<? extends Block> in) {
		return BlockLever.class.isAssignableFrom(in);
	}

	@Override
	public IDependency<BlockPos> getDependencies(IBlockData data) {
		final EnumOrientation orientation = (EnumOrientation)data.getState().getValue(BlockLever.FACING);
		return new FullAndDependency(new BlockPos[]{FacingDependencyUtils.getBlockPos(data.getPos(), orientation.getFacing())});
	}

	

}
