package fr.eyzox.timeline.requirement;

import java.util.Collection;

import fr.eyzox.timeline.Key;


public abstract class RequirementCheckerBase<K,V> implements IRequirementChecker<K,V>{
	public boolean isAvailable(Collection<K> dependenciesLeft, Key<K,V> data) {
		if(dependenciesLeft == null || dependenciesLeft.isEmpty()) return true;
		return _isAvailable(dependenciesLeft, data);
	}
	
	protected abstract boolean _isAvailable(Collection<K> dependenciesLeft, Key<K,V> data);
}
