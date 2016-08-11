package fr.eyzox.bsc.config.option;

import fr.eyzox.bsc.config.option.validator.IValidator;
import fr.eyzox.bsc.exception.InvalidValueException;

public class ConfigOption extends AbstractConfigOption{
	
	private String value;
	
	public ConfigOption(final String name) {
		super(name);
	}
	
	public ConfigOption(final String name, final IValidator validator, final String description, final String defaultValue) {
		super(name, validator, description);
		this.value = defaultValue;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(final String newValue) throws InvalidValueException{
		try {
			if(getValidator() == null || getValidator().isValid(newValue) ) {
				this.value = newValue;
			}else {
				throw new InvalidValueException(newValue);
			}
		}catch(final InvalidValueException e) {
			e.setConfigOption(this);
			throw e;
		}
	}
}
