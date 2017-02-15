package fr.eyzox._new.configoption.events;

import fr.eyzox._new.configoption.ConfigOption;

public class Event<T> {
	private final ConfigOption<T> config;

	public Event(ConfigOption<T> config) {
		this.config = config;
	}

	public ConfigOption<T> getConfig() {
		return config;
	}
	
}
