package fr.eyzox.dependencygraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.dependencygraph.interfaces.IData;
import fr.eyzox.dependencygraph.interfaces.IDependencyProvider;

import java.util.Set;

public class DependencyGraph<KEY, DATA extends IData<KEY>>{

	protected final Map<KEY, Node> index;
	protected final Set<Node> nodes;
	
	protected final List<Node> availables = new ArrayList<Node>();
	private final List<Node> unmodifiableAvailable = Collections.unmodifiableList(availables);

	public DependencyGraph(final Collection<? extends DATA> data, final IDependencyProvider<KEY, DATA> dependencyProvider) {
		//Building index and nodes set
		this.index = new HashMap<KEY, Node>(data.size());
		this.nodes = new HashSet<DependencyGraph<KEY,DATA>.Node>();
		for(final DATA d : data) {
			final Node node = new Node(d);
			nodes.add(node);
			for(final KEY key : d.getKeys()) {
				final Node oldValue = index.put(key, node);
				if(oldValue != null) {
					System.err.println(String.format("[WARN] Duplicated key for values {%s} {%s}", oldValue.data, d));
				}
			}
		}
		
		for(Node node : nodes) {
			node.type = dependencyProvider.provideDependency(node.data);
			node.type.build(this, node);
		}
		
		
		
	}
	
	public DATA poll(final int nextIndex) {

		final Node node = availables.remove(nextIndex);
		
		// Remove from index
		for(final KEY key : node.data.getKeys()) {
			index.remove(key);
		}
		
		for(final Node next : node.requiredBySet) {
			next.type.onElementPolled(this, node, next);
		}
		
		return node.data;
	}

	public boolean hasNext() {
		return !availables.isEmpty();
	}
	
	public List<Node> getAvailables() {
		return unmodifiableAvailable;
	}
	
	protected void buildEdge(final Node node, final KEY dependency) {
		final Node requiredNode = index.get(dependency);
		if(requiredNode == null) {
			throw new RuntimeException("Unable to build edge : the dependency "+dependency+" is not indexed");
		}
		requiredNode.requiredBySet.add(node);
	}
	
	protected class Node {
		private final DATA data;
		private DependencyType type;
		private Set<Node> requiredBySet = new HashSet<Node>();

		protected Node(final DATA data) {
			this.data = data;
		}
		
		protected DATA getData() {
			return data;
		}
		
		protected DependencyType getType() {
			return type;
		}
		
		protected Set<Node> getRequiredBySet() {
			return requiredBySet;
		}
		
		@Override
		public String toString() {
			return new StringBuilder("{data=").append(data.toString()).append(", type=").append(type.toString()).append(", requiredBy=").append(requiredBySet.toString()).append("}").toString();
		}
	}
	
}
