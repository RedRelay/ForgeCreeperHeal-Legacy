package fr.eyzox.dependencygraph;

import java.util.Set;

public interface IDependency<K> {

	public K[] getDependencies();
	
	/**
	 * @param dependenciesLeft - Warning don't remove anything from this set !
	 * @return
	 */
	public boolean isAvailable(final Set<K> dependenciesLeft);
	
}
