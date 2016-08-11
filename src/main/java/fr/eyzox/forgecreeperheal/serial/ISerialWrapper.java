package fr.eyzox.forgecreeperheal.serial;

import net.minecraft.nbt.NBTTagCompound;

public interface ISerialWrapper<DATA> {
	public DATA unserialize(final NBTTagCompound tag);
	public NBTTagCompound serialize(final DATA data);
}
