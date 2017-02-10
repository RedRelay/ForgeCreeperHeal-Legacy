package fr.eyzox._new.configoption.exceptions;

import fr.eyzox._new.configoption.ConfigOption;

public class PropertyValidationException extends ConfigOptionException {

	public <T> PropertyValidationException(ConfigOption<T> configOption, InvalidValueException cause) {
		super("Invalid value for property "+configOption.getName(), cause);
	}

}
