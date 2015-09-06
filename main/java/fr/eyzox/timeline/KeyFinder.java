package fr.eyzox.timeline;

import java.util.Collection;

class KeyFinder<K,V> {
	private K key;

	@Override
	public boolean equals(Object obj) {
		return key.equals(obj instanceof Key ? ((Key) obj).getKey() : obj);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	public boolean containsBlockPos(Collection<Key<K,V>> c, K key) {
		this.key = key;
		boolean b = c.contains(this);
		this.key = null;
		return b;
	}
	
	
}
