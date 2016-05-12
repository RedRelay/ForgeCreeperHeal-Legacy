package fr.eyzox.dependencygraph.type;

import java.util.Set;

import fr.eyzox.dependencygraph.IDependency;

public class MultipleDependency<K> implements IDependencyType {

	private final Set<K> dependencies;
	private final IDependency<K> dependency;
	
	public MultipleDependency(final IDependency<K> dependency, final Set<K> dependencies) {
		this.dependency = dependency;
		this.dependencies = dependencies;
	}

	public Set<K> getDependencies() {
		return dependencies;
	}
	
	public IDependency<K> getDependency() {
		return dependency;
	}

}
