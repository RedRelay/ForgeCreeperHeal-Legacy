package fr.eyzox._new.configoption.events;

import fr.eyzox._new.configoption.ConfigOption;

public abstract class AbstractCollectionChangedEvent<T> extends Event{
    public static enum State {
        ADDED, REMOVED;
    }

    private final State state;

    public AbstractCollectionChangedEvent(ConfigOption<T> config, State state) {
        super(config);
    	this.state = state;
    }

    public State getState() {
        return state;
    }
}
