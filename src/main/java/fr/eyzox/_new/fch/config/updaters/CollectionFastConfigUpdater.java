package fr.eyzox._new.fch.config.updaters;

import java.util.Collection;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.events.ClearedEvent;
import fr.eyzox._new.configoption.events.CollectionChangedEvent;
import fr.eyzox._new.configoption.events.Event;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public abstract class CollectionFastConfigUpdater<T> extends FastConfigUpdater{
    
	public CollectionFastConfigUpdater(ConfigOption<?> c) {
		super(c);
	}

	@Override
    public void applyChanges(FastConfig c, Event value) {
        if(value instanceof CollectionChangedEvent<?>) {
            applyChanges(c, (CollectionChangedEvent<T>)value);
        }else if(value instanceof ClearedEvent<?>) {
            applyChanges(c, (ClearedEvent<T>)value);
        }else {
            super.applyChanges(c, value);
        }
    }

    protected void applyChanges(FastConfig c, CollectionChangedEvent<T> event) {
        final Collection<T> collection = getCollection(c);
        switch (event.getState()) {
            case ADDED:
                collection.add(event.getValue());
                break;
            case REMOVED:
                collection.remove(event.getValue());
                break;

        }
    }
    protected void applyChanges(FastConfig c, ClearedEvent<T> event) {
        final Collection<T> collection = getCollection(c);
        collection.clear();
    }

    protected abstract Collection<T> getCollection(FastConfig c);
}
