package fr.eyzox.forgecreeperheal.dependency;

import java.util.Set;

import net.minecraft.util.math.BlockPos;

public class FullOrDependency extends DefaultDependency {

	public FullOrDependency(BlockPos[] dependencies) {
		super(dependencies);
	}

	@Override
	public boolean isAvailable(Set<BlockPos> dependenciesLeft) {
		return dependenciesLeft.size() < getDependencies().length;
	}

}
