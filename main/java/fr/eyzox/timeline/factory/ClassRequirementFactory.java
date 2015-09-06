package fr.eyzox.timeline.factory;

import fr.eyzox.timeline.Key;
import fr.eyzox.timeline.Timeline;
import fr.eyzox.timeline.requirement.IRequirementChecker;

public abstract class ClassRequirementFactory<K,V> implements IRequirementFactory<K,V> {

	private Class<?> clazz;

	public ClassRequirementFactory(Class<?> clazz) {
		this.clazz = clazz;
	}
	

	@Override
	public IRequirementChecker getRequirement(Key<K,V> blockData) {
		return Timeline.getOnlyOneDependenceRequierement();
	}

	@Override
	public boolean accept(Key<K,V> data) {
		return clazz.isAssignableFrom(getCheckedClass(data));
	}
	
	public Class<?> getCheckedClass(Key<K,V> data) {
		return data.getValue().getClass();
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
