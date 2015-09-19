package fr.eyzox.forgecreeperheal.healtimeline.healable;

import fr.eyzox.timeline.ITimelineElement;
import net.minecraft.world.World;

public interface IHealable extends ITimelineElement{
	void heal(World world, int flag);
	void removeFromWorld(World world);
}
