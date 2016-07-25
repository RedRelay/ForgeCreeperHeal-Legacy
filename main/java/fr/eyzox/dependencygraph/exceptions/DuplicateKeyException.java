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

	private final Map<?, DependencyGraph<?, ? extends IData<?>>.Node> index;
	private final DependencyGraph<?, IData<?>>.Node oldValue, newValue;
	
	public DuplicateKeyException(final Map index, final DependencyGraph.Node oldValue, final DependencyGraph.Node newValue) {
		super("Some DependencyData have the same key");
		this.index = index;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Map<?, DependencyGraph<?, ? extends IData<?>>.Node> getIndex() {
		return index;
	}

	public DependencyGraph<?, IData<?>>.Node getOldValue() {
		return oldValue;
	}

	public DependencyGraph<?, IData<?>>.Node getNewValue() {
		return newValue;
	}
	
}
