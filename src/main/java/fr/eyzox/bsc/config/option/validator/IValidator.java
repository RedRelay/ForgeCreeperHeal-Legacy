package fr.eyzox.bsc.config.option.validator;

import fr.eyzox.bsc.exception.InvalidValueException;

public interface IValidator {
	public boolean isValid(final String value) throws InvalidValueException;
}
