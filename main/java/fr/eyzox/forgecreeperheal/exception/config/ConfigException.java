package fr.eyzox.forgecreeperheal.exception.config;

import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;

public class ConfigException extends ForgeCreeperHealException {

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}
	
	

}
