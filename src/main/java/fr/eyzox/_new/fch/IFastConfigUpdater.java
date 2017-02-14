package fr.eyzox._new.fch;

import fr.eyzox._new.configoption.events.IEvent;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public interface IFastConfigUpdater {
	void applyChanges(FastConfig c, IEvent value);
}
