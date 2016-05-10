package fr.eyzox.forgecreeperheal.config.option.validator;

import fr.eyzox.forgecreeperheal.exception.config.ConfigException;
import fr.eyzox.forgecreeperheal.exception.config.FormatException;
import fr.eyzox.forgecreeperheal.exception.config.InvalidValueException;

public interface IValidator {
	public boolean isValid(final String value) throws InvalidValueException;
}
