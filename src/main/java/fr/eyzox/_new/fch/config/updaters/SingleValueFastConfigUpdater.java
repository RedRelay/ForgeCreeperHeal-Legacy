package fr.eyzox._new.fch.config.updaters;

import fr.eyzox._new.configoption.events.ChangedEvent;
import fr.eyzox._new.configoption.events.IEvent;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public abstract class SingleValueFastConfigUpdater<T> extends FastConfigUpdater {

	@Override
	public void applyChanges(FastConfig c, IEvent value) {
		if(value instanceof ChangedEvent<?>) {
			applyChanges(c, ((ChangedEvent<T>) value).getNewValue());
		}else {
			super.applyChanges(c, value);
		}
	}
	
	protected abstract void applyChanges(FastConfig c, T value);

}
