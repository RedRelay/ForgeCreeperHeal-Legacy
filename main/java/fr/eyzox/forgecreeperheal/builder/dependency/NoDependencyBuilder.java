package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.IDependency;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.dependency.NoDependency;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class NoDependencyBuilder implements IDependencyBuilder{

	final NoDependency noDependency = new NoDependency();
	
	public NoDependencyBuilder() {}

	@Override
	public boolean accept(Class<? extends Block> in) {
		return true;
	}

	@Override
	public IDependency<BlockPos> getDependencies(IBlockData data) {
		return noDependency;
	}

}
