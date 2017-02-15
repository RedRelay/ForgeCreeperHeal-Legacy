package fr.eyzox._new.configoption.events;

import fr.eyzox._new.configoption.ConfigOption;

public class ClearedEvent<T> extends ChangedEvent<T>{
    public ClearedEvent(ConfigOption<T> config, T oldValue, T newValue) {
        super(config, oldValue, newValue);
    }
}
