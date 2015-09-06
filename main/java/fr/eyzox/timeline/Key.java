package fr.eyzox.timeline;

import fr.eyzox.timeline.requirement.IRequirementChecker;

public class Key<K,V> {
	private K key;
	private V value;
	private IRequirementChecker requierement;
	
	public Key(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public IRequirementChecker getRequierement() {
		return requierement;
	}

	public void setRequierement(IRequirementChecker requierement) {
		this.requierement = requierement;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		return key.equals(obj instanceof Key ? ((Key) obj).key : obj);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	
}
