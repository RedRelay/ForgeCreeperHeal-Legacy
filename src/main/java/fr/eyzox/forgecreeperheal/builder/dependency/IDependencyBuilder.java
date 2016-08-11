package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.factory.IData;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public interface IDependencyBuilder extends IData<Block>{
	public DependencyType<BlockPos, BlockData> getDependencies(final BlockData data);
}
