package fr.eyzox.forgecreeperheal.scheduler.custom;

import java.util.Collection;

import fr.eyzox.dependencygraph.RandomDependencyGraph;
import fr.eyzox.dependencygraph.interfaces.IData;
import fr.eyzox.dependencygraph.interfaces.IDependencyProvider;

public class CustomRandomScheduler<K, D extends IData<K>> implements IScheduler<D>{

	private final RandomDependencyGraph<K, D> graph;
	
	public CustomRandomScheduler(Collection<? extends D> c, final IDependencyProvider<K, D> provider) {
		graph = new RandomDependencyGraph<K, D>(c, provider);
	}

	@Override
	public boolean hasNext() {
		return graph.hasNext();
	}

	@Override
	public D next() {
		return graph.poll();
	}
	
	
	
}
