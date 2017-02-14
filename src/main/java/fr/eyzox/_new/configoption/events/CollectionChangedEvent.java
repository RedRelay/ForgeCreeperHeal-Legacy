package fr.eyzox._new.configoption.events;

import java.util.Collection;

public class CollectionChangedEvent<T> extends AbstractCollectionChangedEvent<T> {

    private final Collection<T> collection;
    private final T value;

    public CollectionChangedEvent(Collection<T> collection, T newValue, State state) {
        super(state);
        this.value = newValue;
        this.collection = collection;
    }

    public Collection<T> getCollection() {
        return collection;
    }

    public T getValue() {
        return value;
    }
}
