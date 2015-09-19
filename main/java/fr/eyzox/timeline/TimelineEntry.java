package fr.eyzox.timeline;

class TimelineEntry<V extends ITimelineElement> {

	private final V value;
	
	public TimelineEntry(V value) {
		this.value = value;
	}
	
	public V getValue() {
		return value;
	}

	@Override
	public final boolean equals(Object obj) {
		return value.getKey().equals(obj instanceof ITimelineElement ? ((ITimelineElement) obj).getKey() : obj);
	}

	@Override
	public final int hashCode() {
		return value.getKey().hashCode();
	}
	
}
