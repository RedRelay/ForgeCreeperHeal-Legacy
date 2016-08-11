package fr.eyzox.forgecreeperheal.scheduler.graph.dependency.updater;

import java.util.Set;

import fr.eyzox.dependencygraph.interfaces.IDependencyUpdater;
import net.minecraft.util.BlockPos;

public class FullAndDependency implements IDependencyUpdater<BlockPos> {
	
	@Override
	public boolean isAvailable(Set<BlockPos> dependenciesLeft) {
		return dependenciesLeft.isEmpty();
	}

}
