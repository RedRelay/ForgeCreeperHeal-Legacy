package fr.eyzox._new.configoption;

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
		return super.getValue().put(e.getName(), e);
	}

	public void putAll(Collection<? extends ConfigOption<?>> c) {
		for(ConfigOption<?> e : c) {
			put(e);
		}
	}

	public ConfigOption<?> remove(Object o) {
		if(o instanceof ConfigOption<?>) {
			o = ((ConfigOption<?>)o).getName();
		}
		return super.getValue().remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		boolean hasChanged = false;
		for(Object o : c) {
			if(remove(o) != null) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public void putAll(Map<? extends String, ? extends ConfigOption<?>> m) {
		super.getValue().putAll(m);
	}

	@Override
	public void setValue(Map<String, ConfigOption<?>> value) {
		clear();
		putAll(value);
	}

	
	public void clear() {
		super.getValue().clear();
	}

}
