package fr.eyzox._new.configoption.exceptions;

public class InvalidValueException extends ConfigOptionException {

	public InvalidValueException(Object value) {
		this(value, null);
	}

	public InvalidValueException(Object value, String reason) {
		this("", value, reason);
	}
	
	protected InvalidValueException(String prefix, Object value, String reason) {
		super(prefix+"Invalid value "+value+(reason == null ? "" : (" : "+reason)));
	}

}
