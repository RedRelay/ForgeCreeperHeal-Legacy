package fr.eyzox.dependencygraph;

import java.util.Collection;
import java.util.Random;

public class RandomDependencyGraph<KEY, DATA extends IData<KEY>> extends DependencyGraph<KEY,DATA> {

	private Random rdn;
	
	public RandomDependencyGraph(final Collection<? extends DATA> data, final IDependencyProvider<KEY, DATA> dependencyProvider, final Random rdn) {
		super(data, dependencyProvider);
		this.rdn = rdn;
	}
	
	public RandomDependencyGraph(final Collection<? extends DATA> data, final IDependencyProvider<KEY, DATA> dependencyProvider) {
		this(data, dependencyProvider, new Random());
	}

	@Override
	protected int getNextAvailableIndex() {
		return rdn.nextInt(this.getAvailables().size());
	}

}
