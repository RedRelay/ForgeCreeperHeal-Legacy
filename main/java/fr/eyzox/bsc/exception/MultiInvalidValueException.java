package fr.eyzox.bsc.exception;

import java.util.Collection;
import java.util.LinkedList;

public class MultiInvalidValueException extends InvalidValueException {

	private final Collection<InvalidValueException> exceptions = new LinkedList<InvalidValueException>();
	
	public MultiInvalidValueException(String value) {
		super(value);
	}
	
	public void exception(final InvalidValueException exception) {
		exceptions.add(exception);
	}
	
	@Override
	public String getMessage() {
		final StringBuilder s = new StringBuilder(super.getMessage());
		for(final InvalidValueException e : exceptions) {
			s.append("\t");
			s.append(e.getMessage());
		}
		return s.toString();
	}

	public Collection<InvalidValueException> getExceptions() {
		return exceptions;
	}

}
