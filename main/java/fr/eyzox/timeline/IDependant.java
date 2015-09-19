package fr.eyzox.timeline;

import java.util.Collection;

public interface IDependant {
	Object[] getDependencies();
	boolean isAvailable(Collection<Object> dependenciesLeft);
}
