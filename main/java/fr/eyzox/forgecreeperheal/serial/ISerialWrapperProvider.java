package fr.eyzox.forgecreeperheal.serial;

public interface ISerialWrapperProvider {
	public ISerialWrapper<? extends ISerialWrapperProvider> getSerialWrapper();
}
