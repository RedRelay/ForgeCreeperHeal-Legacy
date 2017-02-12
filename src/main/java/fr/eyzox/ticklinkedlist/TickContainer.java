package fr.eyzox.ticklinkedlist;

public class TickContainer<T> extends Tick {

	
	private T data;
	
	public TickContainer(int tick, T data) {
		super(tick);
		this.data = data;
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
