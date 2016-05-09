package fr.eyzox.forgecreeperheal.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.eyzox.forgecreeperheal.config.option.IConfigOption;

public class ConfigOptionGroup {

	private final String name;
	private final Map<String, IConfigOption> options = new LinkedHashMap<String, IConfigOption>();
	
	public ConfigOptionGroup(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addOption(final IConfigOption option) {
		this.options.put(option.getName(), option);
	}
	
	public void removeOption(final String optionName) {
		this.options.remove(optionName);
	}
	
	public void removeOption(final IConfigOption option) {
		this.removeOption(option.getName());
	}

	public IConfigOption getOption(final String name) {
		return options.get(name);
	}
	
	public Collection<IConfigOption> getOptions() {
		return options.values();
	}
	
}
