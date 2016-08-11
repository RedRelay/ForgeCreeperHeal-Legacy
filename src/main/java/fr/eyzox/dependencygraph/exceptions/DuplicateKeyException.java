package fr.eyzox.dependencygraph.exceptions;

import java.util.Map;

import fr.eyzox.dependencygraph.DataKeyProvider;
import fr.eyzox.dependencygraph.DependencyGraph;
import fr.eyzox.dependencygraph.interfaces.IData;

/**
 * Throws when many {@link DataKeyProvider} have the same key
 * Index contains the new value
 * @author EyZox
 *
 */
public class DuplicateKeyException extends DependencyGraphException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1859970829143171467L;
	private final Map<?, ?> index;
	private final DependencyGraph<?, ?>.Node oldValue, newValue;
	
	public <K, D extends IData<K>> DuplicateKeyException(final Map<K, DependencyGraph<K, D>.Node> index, final DependencyGraph<K, D>.Node oldValue, final DependencyGraph<K, D>.Node newValue) {
		super("Some DependencyData have the same key");
		this.index = index;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public <K, D extends IData<K>> Map<K, DependencyGraph<K, D>.Node> getIndex() {
		return (Map<K, DependencyGraph<K, D>.Node>) index;
	}

	@SuppressWarnings("unchecked")
	public <K, D extends IData<K>> DependencyGraph<K, D>.Node getOldValue() {
		return (DependencyGraph<K, D>.Node) oldValue;
	}

	@SuppressWarnings("unchecked")
	public <K, D extends IData<K>> DependencyGraph<K, D>.Node getNewValue() {
		return (DependencyGraph<K, D>.Node) newValue;
	}
	
}
