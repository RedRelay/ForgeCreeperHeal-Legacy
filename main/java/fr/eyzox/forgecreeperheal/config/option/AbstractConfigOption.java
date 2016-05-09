package fr.eyzox.forgecreeperheal.config.option;

import fr.eyzox.forgecreeperheal.config.option.validator.IValidator;

public abstract class AbstractConfigOption implements IConfigOption{

	private final String name;
	private String description;
	private IValidator validator;
	
	public AbstractConfigOption(final String name) {
		this(name, null, "");
	}
	
	public AbstractConfigOption(final String name, final IValidator validator, final String description) {
		this.name = name;
		this.validator = validator;
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		if(description == null) return;
		this.description = description;
	}
	
	public IValidator getValidator() {
		return validator;
	}
	
	public void setValidator(IValidator validator) {
		this.validator = validator;
	}

}
