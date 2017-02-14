package fr.eyzox._new.configoption;

import fr.eyzox._new.configoption.events.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigOptionGroup extends ConfigOption<Map<String, ConfigOption<?>>> {
	
	public ConfigOptionGroup(String name) {
		super(name, new HashMap<String, ConfigOption<?>>());
	}
	
	@Override
	public Map<String, ConfigOption<?>> getValue() {
		return Collections.unmodifiableMap(super.getValue());
	}
	
	public ConfigOption<?> put(ConfigOption<?> e) {
		final ConfigOption<?> res =  super.getValue().put(e.getName(), e);
		this.fireEvent(new GroupChangedEvent(super.getValue(), new ChangedEvent<ConfigOption<?>>(res, e), CollectionChangedEvent.State.ADDED));
		return res;
	}

	public void putAll(Collection<? extends ConfigOption<?>> c) {
		for(ConfigOption<?> e : c) {
			this.put(e);
		}
	}

	public ConfigOption<?> remove(Object o) {
		if(o instanceof ConfigOption<?>) {
			o = ((ConfigOption<?>)o).getName();
		}
		ConfigOption<?> res = super.getValue().remove(o);
		this.fireEvent(new GroupChangedEvent(super.getValue(), new ChangedEvent<ConfigOption<?>>(res, null), CollectionChangedEvent.State.REMOVED));
		return res;
	}

	public boolean removeAll(Collection<?> c) {
		boolean hasChanged = false;
		for (Object o : c) {
			if (this.remove(o) != null) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@Override
	public void setValue(Map<String, ConfigOption<?>> value) {
		final Map<String, ConfigOption<?>> newValue = new HashMap<String, ConfigOption<?>>(value);
		super.setValue(newValue);
	}

	
	public void clear() {
		final Map<String, ConfigOption<?>> oldValues = new HashMap<String, ConfigOption<?>>(super.getValue());
		super.getValue().clear();
		this.fireEvent(new ClearedEvent<Map<String, ConfigOption<?>>>(oldValues, this.getValue()));
	}

}
