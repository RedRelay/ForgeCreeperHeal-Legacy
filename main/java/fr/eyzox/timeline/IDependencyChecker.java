package fr.eyzox.timeline;

public interface IDependencyChecker<V> {
	boolean isStillRequired(V key);
}
