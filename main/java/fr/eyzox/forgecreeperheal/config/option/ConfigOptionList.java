package fr.eyzox.forgecreeperheal.config.option;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.config.option.validator.IValidator;
import fr.eyzox.forgecreeperheal.exception.config.InvalidConfigValueException;

public class ConfigOptionList extends AbstractConfigOption {

	private List<String> values = new LinkedList<String>();
	
	public ConfigOptionList(final String name) {
		super(name);
	}

	public ConfigOptionList(final String name, final IValidator validator, final String description) {
		super(name, validator, description);
	}
	
	public List<String> getValues() {
		return Collections.unmodifiableList(values);
	}
	
	public void setValues(final List<String> values) throws InvalidConfigValueException{
		if(getValidator() != null) {
			if(values == null) throw new InvalidConfigValueException(this, (this.values != null) ? this.values.toString() : null, null);
			for(final String value : values) {
				try {
					if(!getValidator().isValid(value)) {
						throw new InvalidConfigValueException(this, (this.values != null) ? this.values.toString() : null, value);
					}
				}catch(final Exception e) {
					throw new InvalidConfigValueException(this, (this.values != null) ? this.values.toString() : null, value, e);
				}
			}
		}
		this.values = values;
	}
	
	

}
