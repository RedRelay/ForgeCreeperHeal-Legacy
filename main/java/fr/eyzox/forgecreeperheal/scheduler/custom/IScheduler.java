package fr.eyzox.forgecreeperheal.scheduler.custom;

public interface IScheduler<T> {
	boolean hasNext();
	T next();
}
