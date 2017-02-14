package fr.eyzox._new.configoption.events;

/**
 * Altima Agency
 * Created by aduponchel on 14/02/17.
 */
public class ChangedEvent<T> implements IEvent {
    private final T oldValue, newValue;

    public ChangedEvent(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
