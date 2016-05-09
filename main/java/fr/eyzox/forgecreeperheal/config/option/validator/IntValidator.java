package fr.eyzox.forgecreeperheal.config.option.validator;

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
	public boolean isValid(String value) throws NumberFormatException{
		int intValue = Integer.parseInt(value);
		if(intValue >= minValue && intValue <= maxValue) {
			return true;
		}
		return false;
	}

}
