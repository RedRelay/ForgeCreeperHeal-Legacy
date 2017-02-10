package fr.eyzox._new.configoption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eyzox._new.configoption.exceptions.InvalidValueException;
import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.IValidator;

public class RestrictedConfigOption<T> extends ConfigOption<T> {

	private static final Logger logger = LogManager.getLogger();
	
	private IValidator<T> validator;
	
	public RestrictedConfigOption(String name, T defaultValue) throws PropertyValidationException {
		this(name, defaultValue, null);
	}
	
	public RestrictedConfigOption(String name, T defaultValue, IValidator<T> validator) throws PropertyValidationException {
		super(name, defaultValue);
		this.validator = validator;
		if(validator != null) {
			try {
				validator.isValid(defaultValue);
			} catch (InvalidValueException e) {
				throw new PropertyValidationException(this, e); 
			}
		}
	}
	
	@Override
	public void setValue(T value) {
		try {
			if(validator != null && validator.isValid(value)) {
				super.setValue(value);
			}
		} catch (InvalidValueException e) {
			logger.catching(new PropertyValidationException(this, e));
		}
	}
	
	public void setValidator(IValidator<T> validator) throws PropertyValidationException {
		this.validator = validator;
		if(validator != null) {
			try {
				validator.isValid(getValue());
			} catch (InvalidValueException e) {
				throw new PropertyValidationException(this, e);
			}
		}
	}

	public IValidator<T> getValidator() {
		return this.validator;
	}

}
