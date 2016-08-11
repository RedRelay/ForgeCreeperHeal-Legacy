package fr.eyzox.forgecreeperheal.factory.keybuilder;

public interface IKeyBuilder<KEY> {
	public String convertToString(final KEY key);
	public KEY convertToKey(final String keyStr);
}
