package fr.eyzox.forgecreeperheal.serial;

import fr.eyzox.forgecreeperheal.healable.IHealable;
import fr.eyzox.forgecreeperheal.serial.wrapper.ISerialWrapper;

public interface ISerializableHealable extends IHealable, INBTSerializable{
	public ISerialWrapper<?> getSerialWrapper();
	
}
