package fr.eyzox._new.configoption.events;

import fr.eyzox._new.configoption.ConfigOption;

import java.util.Map;

/**
 * Altima Agency
 * Created by aduponchel on 14/02/17.
 */
public class GroupChangedEvent extends AbstractCollectionChangedEvent<ConfigOption<?>>{
    private final Map<String, ConfigOption<?>> map;
    private final ChangedEvent<ConfigOption<?>> changedEvent;

    public GroupChangedEvent(Map<String, ConfigOption<?>> map, ChangedEvent<ConfigOption<?>> event, State state) {
        super(state);
        this.map = map;
        this.changedEvent = event;
    }

    public Map<String, ConfigOption<?>> getMap() {
        return map;
    }

    public ChangedEvent<ConfigOption<?>> getChangedEvent() {
        return changedEvent;
    }
}
