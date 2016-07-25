package fr.eyzox.forgecreeperheal.serial;

import fr.eyzox.forgecreeperheal.healer.IHealable;
import fr.eyzox.forgecreeperheal.healer.IRemovable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISerializableHealable extends ISerialWrapperProvider, IHealable, IRemovable, INBTSerializable<NBTTagCompound>{

}
