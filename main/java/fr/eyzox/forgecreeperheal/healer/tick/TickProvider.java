package fr.eyzox.forgecreeperheal.healer.tick;

import java.util.Random;

public class TickProvider implements ITickProvider {

	private Random rdn = new Random();
	
	public TickProvider() {}

	@Override
	public int provideTick() {
		//TODO config
		final int maxTick = 5;
		return rdn.nextInt(maxTick);
	}

	@Override
	public int getMinimumTickBeforeHeal() {
		// TODO config
		return 50;
	}

}
