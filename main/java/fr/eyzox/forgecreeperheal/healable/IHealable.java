package fr.eyzox.forgecreeperheal.healable;

import net.minecraft.world.World;

public interface IHealable {
	void heal(World world, int flags);
}
