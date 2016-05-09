package fr.eyzox.forgecreeperheal.exception.config;

import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;

public class InvalidDataTypeException extends ForgeCreeperHealException {

	private final String expected;
	private final String type;
	
	public InvalidDataTypeException(final String expected, final String type) {
		super("Invalid date type "+type+" expected "+expected);
		this.expected = expected;
		this.type = type;
	}
	
	public String getExpected() {
		return expected;
	}
	
	public String getType() {
		return type;
	}

}
