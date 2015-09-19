package fr.eyzox.timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public abstract class Timeline implements Iterable<ITimelineElement>{
	
	protected final ArrayList<TimelineEntry<ITimelineElement>> availables = new ArrayList<TimelineEntry<ITimelineElement>>();
	private final HashMap<Object, LinkedList<TimelineEntry<ITimelineElement>>> waiting = new HashMap<Object, LinkedList<TimelineEntry<ITimelineElement>>>();
	private final Set<TimelineEntry<ITimelineElement>> set;
	
	public Timeline(Collection<? extends ITimelineElement> c) {
		
		set = new HashSet<TimelineEntry<ITimelineElement>>();
		
		for(ITimelineElement element : c) {
			set.add(new TimelineEntry<ITimelineElement>(element));
		}
		
		for(TimelineEntry<ITimelineElement> entry : set) {
			
			
			boolean containerContains = false;
			Object[] dependencies = entry.getValue().getDependencies();
			if(dependencies != null && dependencies.length > 0) {
				for(Object dependency : dependencies) {
					if(new KeyFinder().containsBlockPos(set, dependency)) {
						containerContains = true;
						LinkedList<TimelineEntry<ITimelineElement>> existingDependenciesForAddedData = waiting.get(dependency);
						if(existingDependenciesForAddedData == null) {
							existingDependenciesForAddedData = new LinkedList<TimelineEntry<ITimelineElement>>();
							waiting.put(dependency, existingDependenciesForAddedData);
						}
						existingDependenciesForAddedData.add(entry);
					}
				}
			}
			
			if(!containerContains) {
				availables.add(entry);
			}
			
			
		}
	}
	
	public abstract int selectNext();
	
	
	public ITimelineElement nextAvailable() {
		if(availables.isEmpty()) {
			return null;
		}
		
		int index = selectNext();
		TimelineEntry<ITimelineElement> choosen = availables.get(index);
		
		LinkedList<TimelineEntry<ITimelineElement>> dependencies = waiting.remove(choosen.getValue().getKey());
		if(dependencies == null) {
			availables.remove(index);
		}else {
			Iterator<TimelineEntry<ITimelineElement>> dependenciesIterator = dependencies.iterator();
			TimelineEntry<ITimelineElement> dependency = dependenciesIterator.next();
			if(dependency.getValue().isAvailable(waiting.keySet())){
				availables.set(index, dependency);
			}
			
			while(dependenciesIterator.hasNext()) {
				dependency = dependenciesIterator.next();
				if(dependency.getValue().isAvailable(waiting.keySet())){
					availables.add(dependency);
				}
			}
		}
		set.remove(choosen);
		return choosen.getValue();
	}
	
	public boolean hasAvailable() {
		return !availables.isEmpty();
	}
	
	public Iterator<ITimelineElement> iterator() {
		return new Iterator<ITimelineElement>() {

			Iterator<TimelineEntry<ITimelineElement>> it = set.iterator();
			
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public ITimelineElement next() {
				return it.next().getValue();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
}
