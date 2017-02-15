package fr.eyzox._new.configoption.events;

import java.util.Map;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.ConfigOptionGroup;

public class GroupChangedEvent extends AbstractCollectionChangedEvent<Map<String, ConfigOption<?>>>{
    private final Map<String, ConfigOption<?>> map;
    private final ChangedEvent<ConfigOption<?>> changedEvent;

    public GroupChangedEvent(ConfigOptionGroup config, Map<String, ConfigOption<?>> map, ChangedEvent<ConfigOption<?>> event, State state) {
        super(config, state);
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
