package fr.eyzox.forgecreeperheal.config.option;

import fr.eyzox.forgecreeperheal.config.option.validator.IValidator;
import fr.eyzox.forgecreeperheal.exception.config.InvalidConfigValueException;

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
	
	public void setValue(final String newValue) throws InvalidConfigValueException{
		if(getValidator() != null) {
			try {
				if(!getValidator().isValid(newValue)) {
					throw new InvalidConfigValueException(this, this.value, newValue);
				}
			}catch(final Exception e) {
				throw new InvalidConfigValueException(this, this.value, newValue, e);
			}
		}
		this.value = newValue;
	}
}
