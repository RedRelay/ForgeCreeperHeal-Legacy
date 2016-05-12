package fr.eyzox.dependencygraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.eyzox.dependencygraph.type.IDependencyType;
import fr.eyzox.dependencygraph.type.MultipleDependency;
import fr.eyzox.dependencygraph.type.SingleDependency;

public abstract class DependencyGraph<KEY, DATA extends IData<KEY>>{

	private final Map<KEY, Node> index;
	
	private final List<Node> availables = new ArrayList<Node>();
	private final List<Node> unmodifiableAvailable = Collections.unmodifiableList(availables);

	public DependencyGraph(final Collection<? extends DATA> data, final IDependencyProvider<KEY, DATA> dependencyProvider) {
		this.index = buildIndex(data);
		for(Entry<KEY, Node> entry : index.entrySet()) {
			final Node node = entry.getValue();
			node.type = findDependencyType(dependencyProvider, entry);
			
			if(node.type instanceof SingleDependency) {
				buildEdge(node, ((SingleDependency<KEY>) node.type).getDependency());
			}else if(node.type instanceof MultipleDependency){
				final MultipleDependency<KEY> nodeType = (MultipleDependency<KEY>) node.type;
				for(final KEY dependency : nodeType.getDependencies()) {
					buildEdge(node, dependency);
				}
			}else {
				availables.add(node);
			}
		}
	}

	public DATA next() {

		int nextIndex = getNextAvailableIndex();
		if(nextIndex >= availables.size()) {
			nextIndex = availables.size()-1;
		}
		final Node node = availables.remove(nextIndex);
		
		// Remove from index
		for(final KEY key : node.data.getKeys()) {
			index.remove(key);
		}
		
		
		
		for(final Node next : node.requiredBySet) {
			if(next.type instanceof SingleDependency) {
				availables.add(next);
			}else if(next.type instanceof MultipleDependency) {
				final MultipleDependency<KEY> nextNodeType = (MultipleDependency<KEY>) next.type;
				
				for(final KEY key : node.data.getKeys()) {
					nextNodeType.getDependencies().remove(key);
				}
				
				if(nextNodeType.getDependency().isAvailable(Collections.unmodifiableSet(nextNodeType.getDependencies()))) {
					//Delete this node now available from other dependencies
					for(final KEY dependency : nextNodeType.getDependencies()) {
						index.get(dependency).requiredBySet.remove(next);
					}
					availables.add(next);
				}
			}
		}
		
		return node.data;
	}

	public boolean hasNext() {
		return !availables.isEmpty();
	}
	
	public List<Node> getAvailables() {
		return unmodifiableAvailable;
	}

	protected IDependencyType findDependencyType(final IDependencyProvider<KEY, DATA> dependencyProvider, final Entry<KEY, Node> entry) {
		IDependencyType type = null;

		final IDependency<KEY> dependency = dependencyProvider.provideDependency(entry.getValue().data);
		final KEY[] dependencies = dependency.getDependencies();

		if(dependencies.length > 0){
			final Set<KEY> dependenciesFound = new HashSet<KEY>();
			for(final KEY pos : dependencies) {
				//We retrieve which node is required by this node
				if(index.containsKey(pos)) {
					if(!dependenciesFound.add(pos)) {
						//TODO error : IDependency.getDependencies() has duplicate keys
					}
				}
			}

			
			if(dependenciesFound.size() == 1) {
				type = new SingleDependency<KEY>(dependenciesFound.iterator().next());
			}else if(dependenciesFound.size() > 1){
				type = new MultipleDependency<KEY>(dependency, dependenciesFound);
			}
		}
		return type;
	}
	
	protected void buildEdge(final Node node, final KEY dependency) {
		final Node requiredNode = index.get(dependency);
		if(requiredNode == null) {
			//TODO error
		}
		requiredNode.requiredBySet.add(node);
	}
	
	protected Map<KEY, Node> buildIndex(final Collection<? extends DATA> data) {
		final Map<KEY, Node> index = new HashMap<KEY, Node>(data.size());
		for(final DATA d : data) {
			for(final KEY key : d.getKeys()) {
				if(index.put(key, new Node(d)) != null) {
					//TODO exception
				}
			}
		}
		return index;
	}

	protected abstract int getNextAvailableIndex();
	
	public class Node {
		private final DATA data;
		private IDependencyType type;
		private Set<Node> requiredBySet = new HashSet<Node>();

		public Node(final DATA data) {
			this.data = data;
		}
		
		public DATA getData() {
			return data;
		}
	}

}
