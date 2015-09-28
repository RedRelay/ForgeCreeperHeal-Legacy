package fr.eyzox.forgecreeperheal.healtimeline.healable;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.List;

import fr.eyzox.timeline.ITimelineElement;

public interface IHealable {
	void heal(World world, int flags);
}
