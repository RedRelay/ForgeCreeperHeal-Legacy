package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.NoDependency;
import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class NoDependencyBuilder implements IDependencyBuilder{

	private final NoDependency<BlockPos, BlockData> noDependency = new NoDependency<BlockPos, BlockData>();
	
	public NoDependencyBuilder() {}

	@Override
	public boolean accept(Block in) {
		return true;
	}

	@Override
	public DependencyType<BlockPos, BlockData> getDependencies(BlockData data) {
		return noDependency;
	}

}
