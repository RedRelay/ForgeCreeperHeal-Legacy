package fr.eyzox.forgecreeperheal.config.option.validator;

import fr.eyzox.forgecreeperheal.exception.config.FormatException;
import fr.eyzox.forgecreeperheal.exception.config.InvalidValueException;

public class BooleanValidator implements IValidator {

	private static final BooleanValidator INSTANCE = new BooleanValidator();
	private BooleanValidator() {}
	
	@Override
	public boolean isValid(String value) throws InvalidValueException {
		if(value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE")) {
			return true;
		}else {
			throw new InvalidValueException(value, "<BOOLEAN>");
		}
	}
	
	public static BooleanValidator getInstance() {
		return INSTANCE;
	}

}
