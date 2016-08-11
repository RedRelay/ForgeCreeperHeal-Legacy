package fr.eyzox.dependencygraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.eyzox.dependencygraph.exceptions.DuplicateKeyException;
import fr.eyzox.dependencygraph.interfaces.IData;
import fr.eyzox.dependencygraph.interfaces.IDependencyProvider;

public class DependencyGraph<KEY, DATA extends IData<KEY>>{

	protected final Map<KEY, Node> index;
	protected final Set<Node> nodes;
	
	protected final List<Node> availables = new ArrayList<Node>();
	private final List<Node> unmodifiableAvailable = Collections.unmodifiableList(availables);

	public DependencyGraph(final Collection<? extends DATA> c, final IDependencyProvider<KEY, DATA> dependencyProvider) throws DuplicateKeyException{
		//Building index and nodes set
		this.index = new HashMap<KEY, Node>(c.size());
		this.nodes = new HashSet<DependencyGraph<KEY,DATA>.Node>();
		for(final DATA data : c) {
			final Node node = new Node(data);
			nodes.add(node);
			node.keyProvider.buildIndex(index, node);
		}
		
		for(DependencyGraph<KEY, DATA>.Node node : nodes) {
			node.type = dependencyProvider.provideDependency(node.data);
			node.type.build(this, node);
		}
		
		
		
	}
	
	public DATA poll(final int nextIndex) {

		final DependencyGraph<KEY, DATA>.Node node = availables.remove(nextIndex);
		
		node.keyProvider.removeFromIndex(index, node);
		
		for(final DependencyGraph<KEY, DATA>.Node next : node.requiredBySet) {
			next.type.onElementPolled(this, node, next);
		}
		
		return node.data;
	}

	public boolean hasNext() {
		return !availables.isEmpty();
	}
	
	public List<DependencyGraph<KEY, DATA>.Node> getAvailables() {
		return unmodifiableAvailable;
	}
	
	protected void buildEdge(final DependencyGraph<KEY, DATA>.Node node, final KEY dependency) {
		final Node requiredNode = index.get(dependency);
		if(requiredNode == null) {
			throw new RuntimeException("Unable to build edge : the dependency "+dependency+" is not indexed");
		}
		requiredNode.requiredBySet.add(node);
	}
	
	public class Node {
		private final DATA data;
		private final DataKeyProvider<KEY> keyProvider;
		private DependencyType<KEY,DATA> type;
		private Set<Node> requiredBySet = new HashSet<Node>();

		protected Node(final DATA data) {
			this.data = data;
			this.keyProvider = data.getDataKeyProvider();
			
			if(this.keyProvider == null) {
				throw new NullPointerException();
			}
		}
		
		public DATA getData() {
			return data;
		}
		
		public DataKeyProvider<KEY> getKeyProvider() {
			return keyProvider;
		}
		
		public DependencyType<KEY,DATA> getType() {
			return type;
		}
		
		public Set<Node> getRequiredBySet() {
			return requiredBySet;
		}
		
		@Override
		public String toString() {
			return new StringBuilder("{data=").append(data.toString()).append(", type=").append(type.toString()).append(", requiredBy=").append(requiredBySet.toString()).append("}").toString();
		}
	}
	
}
