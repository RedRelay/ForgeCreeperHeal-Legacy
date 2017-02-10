package fr.eyzox._new.configoption.validator;

import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public interface IValidator<T> {
	public boolean isValid(T obj) throws InvalidValueException;
}
