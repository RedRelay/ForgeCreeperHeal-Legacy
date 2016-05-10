package fr.eyzox.forgecreeperheal.config.option.validator;

import fr.eyzox.forgecreeperheal.exception.config.FormatException;
import fr.eyzox.forgecreeperheal.exception.config.InvalidValueException;

public class IntValidator implements IValidator {

	private final int minValue, maxValue;
	
	public IntValidator(final int minValue, final int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public IntValidator() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public boolean isValid(String value) throws InvalidValueException{
		int intValue = 0;
		
		try {
			intValue = Integer.parseInt(value);
		}catch(NumberFormatException e) {
			throw new InvalidValueException(value, "<INTEGER>", e);
		}
		
		if(intValue >= minValue && intValue <= maxValue) {
			return true;
		}else {
			throw new InvalidValueException(value, "INTEGER between ["+minValue+", "+maxValue+"]");
		}
	}

}
