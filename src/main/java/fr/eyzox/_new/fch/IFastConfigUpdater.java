package fr.eyzox._new.fch;

import fr.eyzox.forgecreeperheal.config.FastConfig;

public interface IFastConfigUpdater<T> {
	void applyChanges(FastConfig c, T value);
}
