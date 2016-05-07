package fr.eyzox.forgecreeperheal.builder;

import fr.eyzox.forgecreeperheal.factory.IData;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import net.minecraft.nbt.NBTTagCompound;

public interface ISerializableHealableBuilder<KEY> extends IData<KEY>{
	public ISerializableHealable create(final NBTTagCompound tag);
}
