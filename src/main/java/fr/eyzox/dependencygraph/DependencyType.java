package fr.eyzox.dependencygraph;

import fr.eyzox.dependencygraph.interfaces.IData;

public abstract class DependencyType<KEY, DATA extends IData<KEY>> {
	
	DependencyType() {}
	
	protected abstract void build(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node theNode);
	protected abstract void onElementPolled(final DependencyGraph<KEY, DATA> graph, final DependencyGraph<KEY, DATA>.Node polledNode, final DependencyGraph<KEY, DATA>.Node theNode);
}
