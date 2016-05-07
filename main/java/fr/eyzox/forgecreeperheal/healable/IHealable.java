package fr.eyzox.forgecreeperheal.healable;

import fr.eyzox.forgecreeperheal.serial.INBTSerializable;
import net.minecraft.world.World;

public interface IHealable extends INBTSerializable{
	void heal(World world, int flags);
}
