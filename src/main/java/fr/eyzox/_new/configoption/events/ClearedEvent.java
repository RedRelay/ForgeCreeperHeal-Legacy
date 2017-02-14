package fr.eyzox._new.configoption.events;

/**
 * Altima Agency
 * Created by aduponchel on 14/02/17.
 */
public class ClearedEvent<T> extends ChangedEvent<T>{
    public ClearedEvent(T oldValue, T newValue) {
        super(oldValue, newValue);
    }
}
