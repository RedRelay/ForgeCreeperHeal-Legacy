package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import fr.eyzox.ticklinkedlist.AbstractTickContainerLinkedList;
import fr.eyzox.ticklinkedlist.TickContainer;

public class HealTask extends AbstractTickContainerLinkedList<Collection<BlockData>> {

	@Override
	protected Collection<BlockData> merge(Collection<BlockData> o1, Collection<BlockData> o2) {
		o2.addAll(o1);
		return o2;
	}
	
	protected LinkedList<TickContainer<Collection<BlockData>>> getLinkedList() {
		return list;
	}

	public void add(int tick, BlockData data) {
		Collection<BlockData> c = new ArrayList<BlockData>(1);
		c.add(data);
		add(tick, c);
	}
}
