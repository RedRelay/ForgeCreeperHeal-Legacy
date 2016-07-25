package fr.eyzox.ticktimeline;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TickTimeline<DATA> {

	private final List<Node<Collection<DATA>>> timeline;
	
	public TickTimeline() {
		this(new LinkedList<Node<Collection<DATA>>>());
	}
	
	public TickTimeline(List<Node<Collection<DATA>>> timeline) {
		this.timeline = timeline;
	}
	
	public void add(final Node<? extends DATA> data) {
		this.add(timeline.listIterator(), data);
	}
	
	/**
	 * 
	 * @param dataCollection MUST BE SORTED
	 * 
	 * Example :
	 * We'll use letters for data.
	 * Input : {(1,A), (1,B), (3,C), (2,D)}
	 * Output :
	 * A -> 1 tick
	 * B -> 2 ticks (A ticks + B ticks)
	 * C -> 5 ticks (A ticks + B ticks + C ticks)
	 * D -> 7 ticks (A ticks + B ticks + C ticks + D ticks)
	 * 
	 */
	public void add(final Collection<Node<DATA>> dataCollection) {
		final ListIterator<Node<Collection<DATA>>> timelineIterator = timeline.listIterator();
		for(final Node<DATA> data : dataCollection) {
			this.add(timelineIterator, data);
		}
	}
	
	protected void add(final ListIterator<Node<Collection<DATA>>> timelineIterator, final Node<? extends DATA> toAdd) {
		
		int tick = toAdd.getTick() < 1 ? 0 : toAdd.getTick();
		final DATA data = toAdd.getData();
		
		//Find the TickContainer to fill
		Node<Collection<DATA>> container = null;
		
		if(tick == 0 && timelineIterator.hasPrevious()) {
			container = timelineIterator.previous();
		}else if(tick == 0 || !timelineIterator.hasNext()) {
			container = new Node<Collection<DATA>>();
			timelineIterator.add(container);
			//timelineIterator.next();
		}else {
			while(container == null && timelineIterator.hasNext()){
				Node<Collection<DATA>> current = timelineIterator.next();
				
				int diff = tick - current.getTick();
				
				if(diff > 0) {
					tick = diff;
				}else if(diff == 0) {
					container = current;
				}else {
					container = new Node<Collection<DATA>>();
					timelineIterator.set(container);
					current.setTick(current.getTick()-tick);
					timelineIterator.add(current);
				}
			}
			
			if(container == null) {
				container = new Node<Collection<DATA>>();
				timelineIterator.add(container);
				//timelineIterator.next();
			}
		}
		
		container.setTick(tick);
		if(container.getData() == null) {
			container.setData(new LinkedList<DATA>());
		}
		container.getData().add(data);
	}
	
	public List<Node<Collection<DATA>>> getTimeline() {
		return timeline;
	}
	
	public Collection<DATA> tick() {
		if(timeline.isEmpty()) {
			return null;
		}
		
		final Node<Collection<DATA>> container = timeline.get(0);
		if(container.getTick() > 1) {
			container.setTick(container.getTick()-1);
			return null;
		}
		
		timeline.remove(0);
		return container.getData();
	}
	
	public boolean isEmpty() {
		return timeline.isEmpty();
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append("{");
		for(final Node<Collection<DATA>> node : this.timeline) {
			s.append(node.toString());
		}
		s.append("}");
		return s.toString();
	}

}
