package fr.eyzox.timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public abstract class AbstractTimeline implements Iterable<ITimelineElement>{
	
	private final Map<Object, ITimelineElement> allEntry;
	
	private final ArrayList<ITimelineElement> availables = new ArrayList<ITimelineElement>();
	private final WaitingElementContainer waitings = new WaitingElementContainer();
	
	private final TimelineElementRemover remover = new TimelineElementRemover();
	
	public AbstractTimeline(Collection<? extends ITimelineElement> c) {
		allEntry = new HashMap<Object, ITimelineElement>(c.size());
		
		final AllEntryBuilder allEntryBuilder = new AllEntryBuilder();
		
		for(ITimelineElement element : c) {
			allEntryBuilder.process(element);
		}
		
		
		final DependencyBuilder dependencyBuilder = new DependencyBuilder();
		
		for(ITimelineElement entry : allEntry.values()) {
			dependencyBuilder.process(entry);
		}
	}
	
	protected abstract int getNextAvailableIndex(List<ITimelineElement> availables);
	
	public ITimelineElement getNextAvailable() {
		return availables.get(this.getNextAvailableIndex(availables));
	}
	
	public ITimelineElement pollNextAvailable() {
		if(availables.isEmpty()) {
			return null;
		}
	
		final ITimelineElement choosen = availables.remove(getNextAvailableIndex(availables));
		
		remover.process(choosen);
		
		return choosen;
	}
	
	public boolean hasAvailable() {
		return !availables.isEmpty();
	}
	
	
	
	@Override
	public Iterator<ITimelineElement> iterator() {
		return allEntry.values().iterator();
	}



	private class AllEntryBuilder implements ICollector<Object>{

		private ITimelineElement element;
		
		public void collect(Object key) {
			allEntry.put(key, element);
		}

		public void process(ITimelineElement element) {
			this.element = element;
			this.element.collectKeys(this);
		}

	}
	
	private class DependencyBuilder implements ICollector<Object> {

		private ITimelineElement element;
		private boolean hasDependency = false;
		
		@Override
		public void collect(Object value) {
			
			ITimelineElement dependency = allEntry.get(value);
			if(dependency == null) {
				return;
			}
			
			hasDependency = true;
			waitings.put(element, dependency);
		}

		private void process(ITimelineElement element) {
			this.element = element;
			this.hasDependency = false;
			
			this.element.collectDependenciesKeys(this);
			if(!hasDependency) {
				availables.add(element);
			}
		}
		
	}
	
	private class TimelineElementRemover implements ICollector<Object> {
		
		@Override
		public void collect(Object value) {
			ITimelineElement removedElement = allEntry.remove(value);
			if(removedElement != null) {
				final Set<ITimelineElement> oldWaitings = waitings.setDependencyAvailable(removedElement);
				if(oldWaitings != null) {
					for(ITimelineElement waitingElement : oldWaitings) {
						if(waitingElement.isAvailable(waitings)) {
							availables.add(waitingElement);
						}
					}
				}
			}else {
				ForgeCreeperHeal.getLogger().warn("TIMELINE - remove a null element (key : "+value+")");
			}
			
		}
		
		private void process(ITimelineElement element) {
			element.collectKeys(this);
		}
		
	}
	
	@Override
	public String toString() {
		return "[\n\tALL-ENTRY:"+allEntry+";\n\tAVAILABLES:"+availables+";\n\tWAITINGS:"+waitings+";\n]";
	}
	
}
