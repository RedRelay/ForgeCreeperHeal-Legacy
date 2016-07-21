package fr.eyzox.forgecreeperheal.serial;

import fr.eyzox.forgecreeperheal.healable.IHealable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISerializableHealable extends ISerialWrapperProvider, IHealable, INBTSerializable<NBTTagCompound>{

}
