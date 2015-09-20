package fr.eyzox.forgecreeperheal.healtimeline.healable;

import net.minecraft.world.World;
import fr.eyzox.timeline.ITimelineElement;

public interface IHealable extends ITimelineElement{
	void heal(World world, int flags);
	void removeFromWorld(World world);
}
