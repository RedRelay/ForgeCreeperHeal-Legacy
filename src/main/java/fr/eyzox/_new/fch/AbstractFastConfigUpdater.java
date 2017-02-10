package fr.eyzox._new.fch;

import fr.eyzox._new.configoption.ConfigOption;

public abstract class AbstractFastConfigUpdater<T> implements IFastConfigUpdater{
	private final ConfigOption<T> configOption;

	public AbstractFastConfigUpdater(ConfigOption<T> configOption) {
		this.configOption = configOption;
	}
	
	public ConfigOption<T> getConfigOption() {
		return configOption;
	}
	
}
