package com.lothrazar.creeperheal.data;


public class TickComparable implements Comparable<TickComparable> {

	private int tick;
	
	public TickComparable() {}
	
	public TickComparable(int tick) {
		this.tick = tick;
	}
	
	@Override
	public int compareTo(TickComparable o) {
		return tick - o.tick;
	}
	
	public int compareTo(int tick) {
		return this.tick - tick;
	}

	public void tick() {
		tick--;

	}

	public boolean isExpired() {
		return tick <= 0;
	}

	public void decrease(TickComparable o) {
		if(o instanceof TickComparable) {
			this.tick = this.tick - ((TickComparable)o).tick;
		}
	}
	
	public int getTick() {
		return tick;
	}
	
	public void setTick(int tick) {
		this.tick = tick;
	}

}
