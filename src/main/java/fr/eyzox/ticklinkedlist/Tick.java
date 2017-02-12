package fr.eyzox.ticklinkedlist;


public class Tick implements Comparable<Tick> {

	private int tick;
	
	public Tick() {}
	
	public Tick(int tick) {
		this.tick = tick;
	}
	
	@Override
	public int compareTo(Tick o) {
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

	public void decrease(Tick o) {
		if(o instanceof Tick) {
			this.tick = this.tick - ((Tick)o).tick;
		}
	}
	
	public int getTick() {
		return tick;
	}
	
	public void setTick(int tick) {
		this.tick = tick;
	}

}
