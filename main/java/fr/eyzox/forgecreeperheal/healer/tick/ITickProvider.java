package fr.eyzox.forgecreeperheal.healer.tick;

public interface ITickProvider {
	public int provideTick();
	public int getMinimumTickBeforeHeal();
}
