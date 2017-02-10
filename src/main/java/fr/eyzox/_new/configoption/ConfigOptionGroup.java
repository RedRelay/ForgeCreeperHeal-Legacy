package fr.eyzox._new.configoption;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.eyzox._new.fch.AbstractConfigOption;
import fr.eyzox._new.fch.IFastConfigUpdater;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public class ConfigOptionGroup extends ConfigOption<Map<String, ConfigOption<?>>> {

	public ConfigOptionGroup(String name) {
		super(name, new HashMap<String, ConfigOption<?>>());
	}
	
	@Override
	public Map<String, ConfigOption<?>> getValue() {
		return Collections.unmodifiableMap(super.getValue());
	}
	
	public void add(ConfigOption<?> configOption) {
		super.getValue().put(configOption.getName(), configOption);
	}
	
	public ConfigOption<?> remove(String configName) {
		return super.getValue().remove(configName);
	}
	
	public void clear() {
		super.getValue().clear();
	}

}
