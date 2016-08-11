package fr.eyzox.dependencygraph;

import java.util.Collection;
import java.util.Map;

import fr.eyzox.dependencygraph.exceptions.DuplicateKeyException;
import fr.eyzox.dependencygraph.interfaces.IData;

public final class SingleDataKeyProvider<K> extends DataKeyProvider<K> {

	private final K key;
	
	public SingleDataKeyProvider(final K key) {
		this.key = key;
	}
	
	@Override
	protected <D extends IData<K>> void buildIndex(final Map<K, DependencyGraph<K, D>.Node> index, final DependencyGraph<K, D>.Node theNode) throws DuplicateKeyException{
		final DependencyGraph<K, D>.Node oldValue = index.put(key, theNode);
		if(oldValue != null) {
			throw new DuplicateKeyException(index, oldValue, theNode);
		}
		
	}

	@Override
	protected <D extends IData<K>> void removeFromIndex(Map<K, DependencyGraph<K, D>.Node> index, DependencyGraph<K, D>.Node theNode) {
		index.remove(key);
	}

	@Override
	protected void removeKeyFrom(Collection<? extends K> c) {
		c.remove(key);
	}

	

	
	
}
