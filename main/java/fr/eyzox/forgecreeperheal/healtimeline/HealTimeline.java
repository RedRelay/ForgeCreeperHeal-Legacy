package fr.eyzox.forgecreeperheal.healtimeline;

import java.util.Collection;
import java.util.Random;

import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.timeline.Timeline;


public class HealTimeline extends Timeline {

	private final static Random rdn = new Random();
	private final World world;
	private int tickLeftBeforeNextHeal;
	
	public HealTimeline(World world, Collection<IHealable> c) {
		super(c);
		this.world = world;
		this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal() + ForgeCreeperHeal.getConfig().getRandomTickVar();
	}
	
	public void onTick() {
		tickLeftBeforeNextHeal--;
		if(tickLeftBeforeNextHeal < 0) {
			((IHealable)this.nextAvailable()).heal(world, 7);
			this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getRandomTickVar();
		}
	}
	
	@Override
	public int selectNext() {
		return rdn.nextInt(availables.size());
	}
	
}
