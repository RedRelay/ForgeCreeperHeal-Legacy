package fr.eyzox._new.configoption;

import java.util.Collection;

public interface CollectionFactory<C extends Collection<T>, T> {
	C create();
}
