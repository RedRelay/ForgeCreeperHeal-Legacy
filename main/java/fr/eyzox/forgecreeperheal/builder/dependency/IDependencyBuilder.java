package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.IDependency;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.factory.IData;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public interface IDependencyBuilder extends IData<Block>{
	public IDependency<BlockPos> getDependencies(final IBlockData data);
}
