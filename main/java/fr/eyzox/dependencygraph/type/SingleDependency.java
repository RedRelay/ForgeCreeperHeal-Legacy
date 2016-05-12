package fr.eyzox.dependencygraph.type;

public class SingleDependency<K> implements IDependencyType {

	private final K dependency;
	
	public SingleDependency(final K dependency) {
		this.dependency = dependency;
	}

	public K getDependency() {
		return dependency;
	}
	

}
