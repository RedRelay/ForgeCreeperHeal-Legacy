package fr.eyzox.forgecreeperheal.healer;

import net.minecraft.world.World;

public interface IHealable {
	void heal(World world, int flags);
}
