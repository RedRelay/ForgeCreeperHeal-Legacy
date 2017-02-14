package fr.eyzox._new.configoption.events;

import java.util.Collection;

/**
 * Altima Agency
 * Created by aduponchel on 14/02/17.
 */
public class CollectionChangedEvent<T> extends AbstractCollectionChangedEvent<T> {

    private final Collection<T> collection;
    private final T newValue;

    public CollectionChangedEvent(Collection<T> collection, T newValue, State state) {
        super(state);
        this.newValue = newValue;
        this.collection = collection;
    }

    public Collection<T> getCollection() {
        return collection;
    }

    public T getNewValue() {
        return newValue;
    }
}
