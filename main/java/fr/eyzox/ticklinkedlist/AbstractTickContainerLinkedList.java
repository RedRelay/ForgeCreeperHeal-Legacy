package fr.eyzox.ticklinkedlist;

import java.util.LinkedList;
import java.util.ListIterator;

public abstract class AbstractTickContainerLinkedList<T> {
	protected LinkedList<TickContainer<T>> list;
	
	public AbstractTickContainerLinkedList() {
		this.list = new LinkedList<TickContainer<T>>();
	}
	
	public T tick() {
		if(list.isEmpty()) return null;
		TickContainer<T> tc = list.getFirst();
		tc.tick();
		if(tc.isExpired()) {
			return list.poll().getData();
		}
		return null;
	}
	
	public void add(int tick, T data) {
		add(new TickContainer<T>(tick, data));
	}
	
	protected void add(TickContainer<T> tickContainerToAdd) {
		if(list.isEmpty()) {
			list.add(tickContainerToAdd);
		}else {
			for(ListIterator<TickContainer<T>> it = list.listIterator(); it.hasNext();){
				TickContainer<T> current = it.next();
				int compareResult = current.compareTo(tickContainerToAdd);
				if( compareResult < 0) {
					tickContainerToAdd.decrease(current);;
				}else if(compareResult == 0) {
					current.setData(this.merge(tickContainerToAdd.getData(), current.getData()));
					return;
				}else {
					it.set(tickContainerToAdd);
					current.decrease(tickContainerToAdd);
					it.add(current);
					return;
				}
			}

			list.add(tickContainerToAdd);
		}
	}
	
	protected abstract T merge(T o1, T o2);
	
}
