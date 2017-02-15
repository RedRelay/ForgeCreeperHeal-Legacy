package fr.eyzox._new.configoption.events;

import fr.eyzox._new.configoption.ConfigOption;

public class ChangedEvent<T> extends Event {
    private final T oldValue, newValue;

    public ChangedEvent(ConfigOption<T> config, T oldValue, T newValue) {
        super(config);
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
