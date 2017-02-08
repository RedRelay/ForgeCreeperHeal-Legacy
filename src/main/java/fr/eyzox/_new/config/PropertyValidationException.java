package fr.eyzox._new.config;

public class PropertyValidationException extends Exception {

	public <T> PropertyValidationException(ConfigOption<T> configOption, InvalidValueException cause) {
		super("Invalid value for property "+configOption.getName(), cause);
	}

}
