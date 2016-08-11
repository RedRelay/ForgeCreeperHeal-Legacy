package fr.eyzox.forgecreeperheal.scheduler.tick;

public interface ITickProvider {
	public int getNextTick();
	public int getStartTick();
}
