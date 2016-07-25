package fr.eyzox.dependencygraph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.eyzox.dependencygraph.interfaces.IData;
import fr.eyzox.dependencygraph.interfaces.IDependencyUpdater;

public final class MultipleDependency<KEY, DATA extends IData<KEY>> extends DependencyType<KEY, DATA> {
	
	private final Set<KEY> dependencies;
	private final IDependencyUpdater<KEY> dependencyUpdater;
	
	private final Set<KEY> uneditableDependencies;
	
	public MultipleDependency(final Collection<? extends KEY> c, final IDependencyUpdater<KEY> dependencyUpdater) {
		this.dependencies = new HashSet<KEY>(c);
		this.dependencyUpdater = dependencyUpdater;
		
		this.uneditableDependencies = Collections.unmodifiableSet(dependencies);
	}
	
	@Override
	protected void build(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node theNode) {
		boolean flag = false;
		for(final KEY dependency : dependencies) {
			if(graph.index.containsKey(dependency)) {
				graph.buildEdge(theNode, dependency);
				flag = true;
			}
		}
		
		if(!flag) {
			graph.availables.add(theNode);
		}
	}
	
	@Override
	protected void onElementPolled(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node polledNode, final DependencyGraph<KEY, DATA>.Node theNode){
		for(final KEY key : polledNode.getData().getKeys()) {
			dependencies.remove(key);
		}
		
		if(dependencyUpdater.isAvailable(uneditableDependencies)) {
			//Delete this node now available from other dependencies
			for(final KEY dependency : dependencies) {
				DependencyGraph<KEY, DATA>.Node dependencyNode = graph.index.get(dependency);
				if(dependencyNode != null) {
					dependencyNode.getRequiredBySet().remove(polledNode);
				}
			}
			graph.availables.add(theNode);
		}
	}
}