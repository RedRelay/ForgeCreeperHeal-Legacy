package fr.eyzox.timeline;

import java.util.Collection;

class KeyFinder {
	
	private Object key;

	@Override
	public boolean equals(Object obj) {
		return key.equals(obj instanceof TimelineEntry ? ((TimelineEntry<ITimelineElement>) obj).getValue().getKey() : obj);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	public boolean containsBlockPos(Collection<TimelineEntry<ITimelineElement>> c, Object key) {
		this.key = key;
		boolean b = c.contains(this);
		this.key = null;
		return b;
	}
	
	
}
