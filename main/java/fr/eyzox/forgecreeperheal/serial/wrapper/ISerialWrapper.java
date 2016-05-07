package fr.eyzox.forgecreeperheal.serial.wrapper;

import fr.eyzox.forgecreeperheal.builder.ISerializableHealableBuilder;
import fr.eyzox.forgecreeperheal.factory.Factory;

public interface ISerialWrapper<KEY> {
	public String getFactoryKey();
	public Factory<KEY, ? extends ISerializableHealableBuilder<KEY>> getFactory();
}
