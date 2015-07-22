package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collection;
import java.util.LinkedList;

class TickContainer {
	private int ticksLeft;
	private Collection<BlockData> blockDataList;
	
	public TickContainer(int ticks, BlockData b) {
		this(ticks, new LinkedList<BlockData>());
		this.blockDataList.add(b);
	}
	
	public TickContainer(int ticks, Collection<BlockData> blockDataList) {
		this.ticksLeft = ticks;
		this.blockDataList = blockDataList;
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	public void setTicksLeft(int ticksLeft) {
		this.ticksLeft = ticksLeft;
	}

	public Collection<BlockData> getBlockDataList() {
		return blockDataList;
	}
	
	
}
