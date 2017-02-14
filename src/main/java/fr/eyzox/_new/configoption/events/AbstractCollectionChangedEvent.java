package fr.eyzox._new.configoption.events;

public abstract class AbstractCollectionChangedEvent<T> implements IEvent{
    public static enum State {
        ADDED, REMOVED;
    }

    private final State state;

    public AbstractCollectionChangedEvent(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
