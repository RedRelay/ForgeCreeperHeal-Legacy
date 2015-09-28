package fr.eyzox.forgecreeperheal.healtimeline;

import java.util.Collection;

import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.timeline.RandomTimeline;


public class HealTimeline extends RandomTimeline {

	private final World world;
	private int tickLeftBeforeNextHeal;
	
	public HealTimeline(World world, Collection<IHealableBlock> c) {
		super(c);
		this.world = world;
		this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal() + ForgeCreeperHeal.getConfig().getRandomTickVar();
	}
	
	public void onTick() {
		tickLeftBeforeNextHeal--;
		if(tickLeftBeforeNextHeal < 0) {
			((IHealable)this.pollNextAvailable()).heal(world, 7);
			this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getRandomTickVar();
		}
	}
	
}
