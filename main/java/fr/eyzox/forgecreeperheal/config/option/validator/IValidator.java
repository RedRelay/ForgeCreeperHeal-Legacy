package fr.eyzox.forgecreeperheal.config.option.validator;

import fr.eyzox.forgecreeperheal.exception.config.InvalidDataTypeException;

public interface IValidator {
	public boolean isValid(final String value) throws InvalidDataTypeException;
}
