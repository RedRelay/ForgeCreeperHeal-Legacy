package fr.eyzox._new.configoption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eyzox._new.configoption.exceptions.InvalidValueException;
import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.IValidator;

public class RestrictedConfigOption<T> extends ConfigOption<T> implements IRestricted<T>{

	private final Logger logger = LogManager.getLogger(RestrictedConfigOption.class);
	
	private IValidator<T> validator;
	
	public RestrictedConfigOption(String name, T defaultValue) {
		super(name, defaultValue);
	}
	
	public RestrictedConfigOption(String name, T defaultValue, IValidator<T> validator) throws PropertyValidationException {
		this(name, defaultValue);
		this.setValidator(validator);
	}
	
	@Override
	public void setValue(T value) {
		try {
			if(validator != null && validator.isValid(this, value)) {
				super.setValue(value);
			}
		} catch (InvalidValueException e) {
			logger.catching(new PropertyValidationException(this, e));
		}
	}
	
	@Override
	public void setValidator(IValidator<T> validator) throws PropertyValidationException {
		this.validator = validator;
		if(validator != null) {
			try {
				if(!validator.isValid(this, getValue())) {
					throw new InvalidValueException(getValue());
				}
			} catch (InvalidValueException e) {
				throw new PropertyValidationException(this, e);
			}
		}
	}

	@Override
	public IValidator<T> getValidator() {
		return this.validator;
	}

}
