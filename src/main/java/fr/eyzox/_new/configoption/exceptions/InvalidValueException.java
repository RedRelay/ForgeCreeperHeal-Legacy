package fr.eyzox._new.configoption.exceptions;

public class InvalidValueException extends ConfigOptionException {
	
	public InvalidValueException(Object value) {
		this(value, null);
	}

	public InvalidValueException(Object value, String reason) {
		this(value+(reason == null ? "" : (" : "+reason)));
	}
	
	protected InvalidValueException(String msg) {
		super("Invalid value "+(msg == null ? "" : msg));
	}

}
