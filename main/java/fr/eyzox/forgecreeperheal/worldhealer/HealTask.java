package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.ticklinkedlist.AbstractTickContainerLinkedList;
import fr.eyzox.ticklinkedlist.TickContainer;

public class HealTask extends AbstractTickContainerLinkedList<BlockData> {

	@Override
	protected BlockData merge(final BlockData o1, final BlockData o2) {
		BlockData cursor = o1;
		while(cursor.getNext() != null) {
			cursor = cursor.getNext();
		}
		cursor.setNext(o2);
		return o1;
	}
	
	protected LinkedList<TickContainer<BlockData>> getLinkedList() {
		return list;
	}
	
	public void add(List<BlockData> blockDataList) {
		Collections.shuffle(blockDataList);
		int minTick = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal();
		for(BlockData blockData : blockDataList) {
			int tick = minTick + ForgeCreeperHeal.getConfig().getRandomTickVar();
			this.add(tick, blockData);
			minTick = tick;
		}
	}


}
