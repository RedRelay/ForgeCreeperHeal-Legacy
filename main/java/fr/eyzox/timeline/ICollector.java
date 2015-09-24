package fr.eyzox.timeline;

/**
 * A collector 
 * 
 * @author EyZox
 *
 * @param <VALUE>
 */
public interface ICollector<VALUE> {
	void collect(VALUE value);
}
