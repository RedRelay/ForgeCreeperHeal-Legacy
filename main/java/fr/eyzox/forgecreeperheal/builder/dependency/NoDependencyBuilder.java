package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.NoDependency;
import fr.eyzox.dependencygraph.interfaces.IDependencyUpdater;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class NoDependencyBuilder implements IDependencyBuilder{

	private final NoDependency<BlockPos, IBlockData> noDependency = new NoDependency<BlockPos, IBlockData>();
	
	public NoDependencyBuilder() {}

	@Override
	public boolean accept(Block in) {
		return true;
	}

	@Override
	public DependencyType<BlockPos, IBlockData> getDependencies(IBlockData data) {
		return noDependency;
	}

}
