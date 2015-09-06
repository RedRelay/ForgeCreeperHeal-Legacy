package fr.eyzox.timeline;

import java.util.List;

public interface ITimelineSelector {
	public <K,V> int select(List<Key<K,V>> availableItems);
}
