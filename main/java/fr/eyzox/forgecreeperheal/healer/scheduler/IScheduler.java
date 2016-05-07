package fr.eyzox.forgecreeperheal.healer.scheduler;

public interface IScheduler<E> {
	public E next();
	public boolean hasNext();
	
}
