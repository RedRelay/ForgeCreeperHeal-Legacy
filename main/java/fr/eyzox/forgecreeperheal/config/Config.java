package fr.eyzox.forgecreeperheal.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.eyzox.forgecreeperheal.config.option.ConfigOption;
import fr.eyzox.forgecreeperheal.config.option.ConfigOptionList;
import fr.eyzox.forgecreeperheal.config.option.validator.BooleanValidator;
import fr.eyzox.forgecreeperheal.config.option.validator.IValidator;
import fr.eyzox.forgecreeperheal.config.option.validator.IntValidator;
import fr.eyzox.forgecreeperheal.exception.config.FormatException;
import fr.eyzox.forgecreeperheal.exception.config.InvalidValueException;

public class Config {

	private final Map<String, ConfigOptionGroup> groups = new LinkedHashMap<String, ConfigOptionGroup>();
	
	public Config() {}
	
	public void addOptionGroup(final ConfigOptionGroup optionGroup) {
		this.groups.put(optionGroup.getName(), optionGroup);
	}
	
	public void removeOptionGroup(final ConfigOptionGroup optionGroup) {
		this.removeOptionGroup(optionGroup.getName());
	}
	
	public void removeOptionGroup(final String optionGroupeName) {
		this.groups.remove(optionGroupeName);
	}
	
	public ConfigOptionGroup getOptionGroup(final String name) {
		return groups.get(name);
	}
	
	public Collection<ConfigOptionGroup> getOptionGroups() {
		return groups.values();
	}
	
}
