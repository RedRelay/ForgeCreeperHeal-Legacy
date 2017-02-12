package fr.eyzox._new.configoption;

import java.util.Collection;

public interface CollectionFactory<T> {
	Collection<T> create();
}
