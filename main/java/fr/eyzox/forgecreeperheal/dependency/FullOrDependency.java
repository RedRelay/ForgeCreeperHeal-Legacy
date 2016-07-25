package fr.eyzox.forgecreeperheal.dependency;

import java.util.Set;

import fr.eyzox.dependencygraph.interfaces.IDependencyUpdater;
import net.minecraft.util.math.BlockPos;

public class FullOrDependency implements IDependencyUpdater<BlockPos> {

	private final int dependencyNumber;
	
	public FullOrDependency(final int dependencyNumber) {
		this.dependencyNumber = dependencyNumber;
	}
	
	@Override
	public boolean isAvailable(Set<BlockPos> dependenciesLeft) {
		return dependenciesLeft.size() < dependencyNumber;
	}

}
