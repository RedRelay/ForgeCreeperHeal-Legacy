package fr.eyzox._new.config;

public interface IValidator<T> {
	public boolean isValid(T obj) throws InvalidValueException;
}
