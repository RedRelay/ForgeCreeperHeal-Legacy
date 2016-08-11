package fr.eyzox.dependencygraph.interfaces;

import java.util.Set;

public interface IDependencyUpdater<K> {
	
	public boolean isAvailable(final Set<K> dependenciesLeft);
	
}
