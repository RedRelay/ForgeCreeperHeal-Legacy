package fr.eyzox.forgecreeperheal.config.option;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.config.option.validator.IValidator;
import fr.eyzox.forgecreeperheal.exception.config.InvalidValueException;
import fr.eyzox.forgecreeperheal.exception.config.MultiInvalidValue;

public class ConfigOptionList extends AbstractConfigOption {

	private final List<String> values = new LinkedList<String>();
	
	public ConfigOptionList(final String name) {
		super(name);
	}

	public ConfigOptionList(final String name, final IValidator validator, final String description) {
		super(name, validator, description);
	}
	
	public List<String> getValues() {
		return Collections.unmodifiableList(values);
	}
	
	public void setValues(final List<String> values) throws InvalidValueException{
		this.values.clear();
		if(getValidator() != null) {
			final MultiInvalidValue exceptions = new MultiInvalidValue(values.toString());
			for(final String value : values) {
				try {
					if(!getValidator().isValid(value)) {
						exceptions.exception(new InvalidValueException(value));
					}
				}catch(final InvalidValueException e) {
					exceptions.exception(e);
				}
			}
			if(!exceptions.getExceptions().isEmpty()) {
				exceptions.setConfigOption(this);
				throw exceptions;
			}
		}
		
		this.values.addAll(values);
	}
	
	

}
