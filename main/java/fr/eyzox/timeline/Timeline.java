package fr.eyzox.timeline;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.timeline.factory.IRequirementFactory;
import fr.eyzox.timeline.requirement.IRequirementChecker;

public class Timeline<K,V> extends AbstractCollection<Key<K,V>>{
	
	private static IRequirementChecker onlyOneDependence = new IRequirementChecker() {
		@Override
		public boolean isAvailable(Collection dependenciesLeft, Key data) {
			return true;
		}
	};
	
	private static ITimelineSelector defaultSelector;
	
	private LinkedList<IRequirementFactory<K,V>> requirementFactories;
	private ITimelineSelector selector;
	private List<Key<K,V>> availables = new ArrayList<Key<K,V>>();
	private HashMap<K, LinkedList<Key<K,V>>> waiting = new HashMap<K, LinkedList<Key<K,V>>>();
	private final KeyFinder<K,V> KEY_FINDER = new KeyFinder<K,V>();
	
	public Timeline(){
		this(getDefaultSelector());
	}
	
	public Timeline(ITimelineSelector selector) {
		this(selector, new LinkedList<IRequirementFactory<K,V>>());
	}
	
	public Timeline(LinkedList<IRequirementFactory<K,V>> factories) {
		this(getDefaultSelector(), factories);
	}
	
	public Timeline(ITimelineSelector selector, LinkedList<IRequirementFactory<K,V>> factories) {
		this.selector = selector;
		this.requirementFactories = factories;
	}
	
	@Override
	public boolean add(Key<K,V> data) {
		return this.add(data, this);
	}
	
	public boolean add(Key<K,V> data, Collection<Key<K,V>> container) {
		for(IRequirementFactory<K,V> factory : requirementFactories) {
			if(factory.accept(data)) {
				IRequirementChecker req = factory.getRequirement(data);
				if(req == null) {
					req = onlyOneDependence;
				}
				data.setRequierement(req);
				return this.add(data, factory.getKeyDependencies(data), container);
			}
		}
		return add(data, null, container);
	}
	
	public boolean add(Key<K,V> data, K[] keyDependencies, Collection<Key<K,V>> container) {
		boolean containerContains = false;
		if(keyDependencies != null && keyDependencies.length > 0) {
			for(K keyDependency : keyDependencies) {
				if(KEY_FINDER.containsBlockPos(container, keyDependency)) {
					containerContains = true;
					LinkedList<Key<K,V>> dependencies = waiting.get(keyDependency);
					if(dependencies == null) {
						dependencies = new LinkedList<Key<K,V>>();
						waiting.put(keyDependency, dependencies);
					}
					dependencies.add(data);
				}
			}
		}
		
		if(!containerContains) {
			availables.add(data);
		}
		return true;
	}
	
	public boolean addAll(Collection<? extends Key<K,V>> c, Collection<Key<K,V>> container) {
		for(Key data : c) {
			this.add(data, container);
		}
		return !c.isEmpty();
	}
	
	public Key<K,V> poll() {
		int index = selector.select(availables);
		Key choosen = availables.get(index);
		LinkedList<Key<K,V>> dependencies = waiting.remove(choosen.getKey());
		if(dependencies == null) {
			availables.remove(index);
		}else {
			Iterator<Key<K,V>> dependenciesIterator = dependencies.iterator();
			Key dependency = dependenciesIterator.next();
			if(dependency.getRequierement().isAvailable(waiting.keySet(), dependency)){
				availables.set(index, dependency);
			}
			
			while(dependenciesIterator.hasNext()) {
				dependency = dependenciesIterator.next();
				if(dependency.getRequierement().isAvailable(waiting.keySet(), dependency)){
					availables.add(dependency);
				}
			}
		}
		
		return choosen;
	}
	
	public static IRequirementChecker getOnlyOneDependenceRequierement() {
		return onlyOneDependence;
	}
	
	private class TimelineIterator implements Iterator<Key<K,V>>{

		private Iterator<Key<K,V>> itCursor = Timeline.this.availables.iterator();
		private Iterator<LinkedList<Key<K,V>>> waitingIt = Timeline.this.waiting.values().iterator();
		private Collection<Key<K,V>> dependenciesCursor;
		
		@Override
		public boolean hasNext() {
			return itCursor.hasNext() || waitingIt.hasNext();
		}

		@Override
		public Key<K,V> next() {
			if(!itCursor.hasNext()) {
				dependenciesCursor = waitingIt.next();
				itCursor = dependenciesCursor.iterator();
			}
			return itCursor.next();
		}

		@Override
		public void remove() {
			itCursor.remove();
			if(dependenciesCursor != null && dependenciesCursor.isEmpty()) {
				waitingIt.remove();
			}
		}
		
	}

	@Override
	public Iterator<Key<K,V>> iterator() {
		return new TimelineIterator();
	}

	@Override
	public int size() {
		if(availables.isEmpty()) return 0;
		int size = availables.size();
		for(Collection<Key<K,V>> dependencies : waiting.values()) {
			size += dependencies.size();
		}
		return size;
	}

	public LinkedList<IRequirementFactory<K, V>> getRequirementFactories() {
		return requirementFactories;
	}

	public void setRequirementFactories(LinkedList<IRequirementFactory<K, V>> requirementFactories) {
		this.requirementFactories = requirementFactories;
	}
	
	public ITimelineSelector getSelector() {
		return selector;
	}

	public void setSelector(ITimelineSelector selector) {
		this.selector = selector;
	}

	public static ITimelineSelector getDefaultSelector() {
		if(defaultSelector == null) {
			defaultSelector = new ITimelineSelector() {
				@Override
				public <T, Y> int select(List<Key<T, Y>> availableItems) {
					return 0;
				}
			};
		}
		return defaultSelector;
	}
	
	
	
}
