package fr.eyzox._new.configoption.validator;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public interface IValidator<T> {
	/**
	 * @param config the holder
	 * @param obj the new value
	 * @return true if valid, false else
	 * @throws InvalidValueException
	 */
	public boolean isValid(ConfigOption<?> config, T obj) throws InvalidValueException;
}
