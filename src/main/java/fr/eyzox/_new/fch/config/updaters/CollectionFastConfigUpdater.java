package fr.eyzox._new.fch.config.updaters;

import fr.eyzox._new.configoption.events.ClearedEvent;
import fr.eyzox._new.configoption.events.CollectionChangedEvent;
import fr.eyzox._new.configoption.events.IEvent;
import fr.eyzox.forgecreeperheal.config.FastConfig;

import java.util.Collection;

public abstract class CollectionFastConfigUpdater<T> extends FastConfigUpdater{
    @Override
    public void applyChanges(FastConfig c, IEvent value) {
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
