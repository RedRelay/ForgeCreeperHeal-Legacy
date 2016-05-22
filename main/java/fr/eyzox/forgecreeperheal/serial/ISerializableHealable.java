package fr.eyzox.forgecreeperheal.serial;

import fr.eyzox.forgecreeperheal.healable.IHealable;
import fr.eyzox.forgecreeperheal.serial.wrapper.ISerialWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISerializableHealable extends IHealable, INBTSerializable<NBTTagCompound>{
	public ISerialWrapper<?> getSerialWrapper();
	
}
