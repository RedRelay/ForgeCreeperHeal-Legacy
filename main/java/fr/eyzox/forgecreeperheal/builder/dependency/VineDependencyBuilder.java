package fr.eyzox.forgecreeperheal.builder.dependency;

import java.util.ArrayList;
import java.util.List;

import fr.eyzox.dependencygraph.IDependency;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.dependency.FullOrDependency;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class VineDependencyBuilder implements IDependencyBuilder{

	@Override
	public boolean accept(Class<? extends Block> in) {
		return BlockVine.class.isAssignableFrom(in);
	}

	@Override
	public IDependency<BlockPos> getDependencies(IBlockData data) {
		final List<BlockPos> dependencies = new ArrayList<BlockPos>(2);
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			if(((Boolean)data.getState().getValue(BlockVine.getPropertyFor(facing))).booleanValue()) {
				dependencies.add(FacingDependencyUtils.getBlockPos(data.getPos(), facing));
				break;
			}
		}
		if(((Boolean)data.getState().getValue(BlockVine.UP)).booleanValue()) {
			dependencies.add(FacingDependencyUtils.getBlockPos(data.getPos(), EnumFacing.UP));
		}
		
		final BlockPos[] dependencyArray = new BlockPos[dependencies.size()];
		dependencies.toArray(dependencyArray);
		
		return new FullOrDependency(dependencyArray);
	}
}
