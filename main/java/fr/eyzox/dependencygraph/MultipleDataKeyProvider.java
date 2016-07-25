package fr.eyzox.dependencygraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.eyzox.dependencygraph.DependencyGraph.Node;
import fr.eyzox.dependencygraph.exceptions.DuplicateKeyException;
import fr.eyzox.dependencygraph.interfaces.IData;

public final class MultipleDataKeyProvider<K> extends DataKeyProvider<K> {

	private final Set<K> keys;
	
	public MultipleDataKeyProvider(final Collection<? extends K> keys) {
		this.keys = new HashSet<K>(keys);
	}
	
	@Override
	protected void buildIndex(Map<K, DependencyGraph<K, ? extends IData<K>>.Node> index, DependencyGraph<K, ? extends IData<K>>.Node theNode) throws DuplicateKeyException {
		for(final K key : keys) {
			final Node oldValue = index.put(key, theNode);
			if(oldValue != null) {
				throw new DuplicateKeyException(index, oldValue, theNode);
			}
		}
	}

	@Override
	protected void removeFromIndex(Map<K, DependencyGraph<K, ? extends IData<K>>.Node> index, DependencyGraph<K, ? extends IData<K>>.Node theNode) {
		for(K key : keys) {
			index.remove(key);
		}
	}

	@Override
	protected void removeKeyFrom(Collection<? extends K> c) {
		c.removeAll(keys);
	}


}
