package fr.eyzox.forgecreeperheal.dependency;

import java.util.Set;

import net.minecraft.util.math.BlockPos;

public class FullAndDependency extends DefaultDependency {

	public FullAndDependency(BlockPos[] dependencies) {
		super(dependencies);
	}
	
	@Override
	public boolean isAvailable(Set<BlockPos> dependenciesLeft) {
		return dependenciesLeft.isEmpty();
	}

}
