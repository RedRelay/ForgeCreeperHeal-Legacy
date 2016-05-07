package fr.eyzox.forgecreeperheal.serial;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSerializable {
	void writeToNBT(final NBTTagCompound tag);
	void readFromNBT(final NBTTagCompound tag);
}
