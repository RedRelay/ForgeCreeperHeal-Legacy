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
	protected void buildIndex(final Map<K, DependencyGraph<K, ? extends IData<K>>.Node> index, final DependencyGraph<K, ? extends IData<K>>.Node theNode) throws DuplicateKeyException{
		final DependencyGraph<K, ? extends IData<K>>.Node oldValue = index.put(key, theNode);
		if(oldValue != null) {
			throw new DuplicateKeyException(index, oldValue, theNode);
		}
		
	}

	@Override
	protected void removeFromIndex(Map<K, DependencyGraph<K, ? extends IData<K>>.Node> index, DependencyGraph<K, ? extends IData<K>>.Node theNode) {
		index.remove(key);
	}

	@Override
	protected void removeKeyFrom(Collection<? extends K> c) {
		c.remove(key);
	}

	

	
	
}
