package fr.eyzox.forgecreeperheal.exception;

public class ForgeCreeperHealException extends RuntimeException {

	public ForgeCreeperHealException() {
	}

	public ForgeCreeperHealException(String message) {
		super(message);
	}

	public ForgeCreeperHealException(Throwable cause) {
		super(cause);
	}

	public ForgeCreeperHealException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForgeCreeperHealException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
