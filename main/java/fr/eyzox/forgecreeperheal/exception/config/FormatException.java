package fr.eyzox.forgecreeperheal.exception.config;

import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;

public class FormatException extends ConfigException {
	
	public FormatException(final String type, final String expected) {
		this(type, expected, null);
	}
	
	public FormatException(final String type, final String expected, final Throwable cause) {
		super(new StringBuilder("Invalid data type : \"").append(type).append("\" expected : \"").append(expected).append("\"").toString(), cause);
	}

}
