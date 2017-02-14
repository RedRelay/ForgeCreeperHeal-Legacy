package fr.eyzox._new.configoption.events;

public class ClearedEvent<T> extends ChangedEvent<T>{
    public ClearedEvent(T oldValue, T newValue) {
        super(oldValue, newValue);
    }
}
