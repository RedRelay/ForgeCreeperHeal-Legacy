package fr.eyzox.dependencygraph.interfaces;

import fr.eyzox.dependencygraph.DependencyType;

public interface IDependencyProvider<KEY, DATA extends IData<KEY>> {
	public DependencyType<KEY, DATA> provideDependency(final DATA data);
}
