package fr.eyzox.dependencygraph;

public interface IDependencyProvider<KEY, DATA extends IData<KEY>> {
	public IDependency<KEY> provideDependency(final DATA data);
}
