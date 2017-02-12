package fr.eyzox._new.configoption;

import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.IValidator;

public interface IRestricted<T> {
	void setValidator(IValidator<T> validator) throws PropertyValidationException;
	IValidator<T> getValidator();
}
