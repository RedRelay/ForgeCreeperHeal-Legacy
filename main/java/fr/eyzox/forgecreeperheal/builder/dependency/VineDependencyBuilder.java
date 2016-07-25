package fr.eyzox.forgecreeperheal.builder.dependency;

import java.util.ArrayList;
import java.util.List;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.MultipleDependency;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.scheduler.graph.dependency.updater.FullOrDependency;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class VineDependencyBuilder implements IDependencyBuilder{

	@Override
	public boolean accept(Block in) {
		return BlockVine.class.isAssignableFrom(in.getClass());
	}

	@Override
	public DependencyType<BlockPos, IBlockData> getDependencies(IBlockData data) {
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
		
		return new MultipleDependency<BlockPos, IBlockData>(dependencies, new FullOrDependency(dependencies.size()));
	}
}
