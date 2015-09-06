package fr.eyzox.timeline.factory;

import fr.eyzox.timeline.Key;
import fr.eyzox.timeline.requirement.IRequirementChecker;

public interface IRequirementFactory<K,V> {
	public boolean accept(Key<K,V> data);
	public K[] getKeyDependencies(Key<K,V> data);
	public IRequirementChecker<K,V> getRequirement(Key<K,V> data);
}
