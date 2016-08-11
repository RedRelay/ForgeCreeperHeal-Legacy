package fr.eyzox.dependencygraph;

import fr.eyzox.dependencygraph.interfaces.IData;

public final class SingleDependency<KEY, DATA extends IData<KEY>> extends DependencyType<KEY, DATA> {
	
	private final KEY dependency;
	
	public SingleDependency(final KEY dependency) {
		this.dependency = dependency;
	}
	
	protected void build(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node theNode) {
		if(graph.index.containsKey(dependency)){
			graph.buildEdge(theNode, dependency);
		}else {
			graph.availables.add(theNode);
		}
	}
	
	protected void onElementPolled(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node polledNode, final DependencyGraph<KEY, DATA>.Node theNode){
		graph.availables.add(theNode);
	}
}
