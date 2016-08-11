package fr.eyzox.dependencygraph;

import fr.eyzox.dependencygraph.interfaces.IData;

public final class NoDependency<KEY, DATA extends IData<KEY>> extends DependencyType<KEY, DATA> {
	
	@Override
	protected void build(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node theNode) {
		graph.availables.add(theNode);
	}
	
	@Override
	protected void onElementPolled(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node polledNode, final DependencyGraph<KEY, DATA>.Node theNode){
	}
}
