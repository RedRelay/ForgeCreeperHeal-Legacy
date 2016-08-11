package fr.eyzox.bsc.config.option.validator;

import fr.eyzox.bsc.exception.InvalidValueException;

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
