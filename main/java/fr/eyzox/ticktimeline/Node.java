package fr.eyzox.ticktimeline;

public class Node<W> {
	
	private int tick;
	private W data;
	
	public Node() {}
	
	public int getTick() {
		return tick;
	}
	public void setTick(final int tick) {
		this.tick = tick < 1 ? 0 : tick;
	}
	public W getData() {
		return data;
	}
	public void setData(final W data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + tick;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (tick != other.tick)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder("[tick=").append(tick).append(", data=").append(data).append("]").toString();
	}
}
