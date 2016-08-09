package fr.eyzox.forgecreeperheal.scheduler.tick;

import java.util.Random;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class TickProvider implements ITickProvider {

	private Random rdn = new Random();
	
	public TickProvider() {}

	@Override
	public int getNextTick() {
		return random(ForgeCreeperHeal.getConfig().getMinTick(), ForgeCreeperHeal.getConfig().getMaxTick());
	}

	@Override
	public int getStartTick() {
		return random(ForgeCreeperHeal.getConfig().getMinTickStart(), ForgeCreeperHeal.getConfig().getMaxTickStart());
	}
	
	private int random(int min, int max) {
		max = max-min;
		return min + (max > 0 ? rdn.nextInt(max) : 0);
	}

}
