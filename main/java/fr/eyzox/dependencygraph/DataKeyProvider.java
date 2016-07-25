package fr.eyzox.dependencygraph;

import java.util.Collection;
import java.util.Map;

import fr.eyzox.dependencygraph.exceptions.DuplicateKeyException;
import fr.eyzox.dependencygraph.interfaces.IData;

public abstract class DataKeyProvider<K> {

	DataKeyProvider() {
	}
	
	protected abstract void buildIndex(final Map<K, DependencyGraph<K, ? extends IData<K>>.Node> index, final DependencyGraph<K, ? extends IData<K>>.Node theNode) throws DuplicateKeyException;
	protected abstract void removeFromIndex(final Map<K, DependencyGraph<K, ? extends IData<K>>.Node> index, final DependencyGraph<K, ? extends IData<K>>.Node theNode);
	protected abstract void removeKeyFrom(final Collection<? extends K> c);
}
