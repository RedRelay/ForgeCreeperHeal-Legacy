package fr.eyzox.timeline.requirement;

import java.util.Collection;

import fr.eyzox.timeline.Key;

public interface IRequirementChecker<K,V> {
	public boolean isAvailable(Collection<K> dependenciesLeft, Key<K,V> data);
}
