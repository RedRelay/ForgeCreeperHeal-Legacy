package fr.eyzox.forgecreeperheal.config.option.validator;

import fr.eyzox.forgecreeperheal.exception.config.InvalidDataTypeException;

public class BooleanValidator implements IValidator {

	private static final BooleanValidator INSTANCE = new BooleanValidator();
	private BooleanValidator() {}
	
	@Override
	public boolean isValid(String value) throws InvalidDataTypeException {
		if(value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE")) {
			return true;
		}else {
			throw new InvalidDataTypeException("BOOLEAN", value);
		}
	}
	
	public static BooleanValidator getInstance() {
		return INSTANCE;
	}

}
