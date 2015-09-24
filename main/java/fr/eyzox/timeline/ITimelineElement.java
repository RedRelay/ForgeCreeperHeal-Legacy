package fr.eyzox.timeline;

public interface ITimelineElement {
	
	/**
	 * Collect all keys from the element
	 * @param collector
	 */
	void collectKeys(ICollector<Object> collector);
	
	/**
	 * Collect all dependencies keys for the element
	 * @param collector
	 */
	void collectDependenciesKeys(ICollector<Object> collector);
	
	/**
	 * Return the element state
	 * @param checker a Checker to check dependencies states
	 * @return true if available, else false
	 */
	boolean isAvailable(IDependencyChecker<Object> checker);
}
