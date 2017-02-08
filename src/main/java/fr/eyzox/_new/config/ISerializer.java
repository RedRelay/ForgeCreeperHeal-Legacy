package fr.eyzox._new.config;

public interface ISerializer<T> {
	void read(T to);
	void write(T from);
}
