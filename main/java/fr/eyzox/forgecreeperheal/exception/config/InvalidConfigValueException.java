package fr.eyzox.forgecreeperheal.exception.config;

import fr.eyzox.forgecreeperheal.config.option.IConfigOption;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;

public class InvalidConfigValueException extends ForgeCreeperHealException {

	private final IConfigOption configOption;
	private final String oldValue;
	private final String newValue;
	
	public InvalidConfigValueException(final IConfigOption configOption, final String oldValue, final String newValue) {
		this(configOption, oldValue, newValue, null);
	}
	
	public InvalidConfigValueException(final IConfigOption configOption, final String oldValue, final String newValue, final Throwable cause) {
		super(String.format("%s is not a valid value for %s", newValue, configOption.getName()), cause);
		this.configOption = configOption;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public IConfigOption getConfigOption() {
		return configOption;
	}
	
	public String getOldValue() {
		return oldValue;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
}
