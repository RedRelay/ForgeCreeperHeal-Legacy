package fr.eyzox._new.fch.config.updaters;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.events.ChangedEvent;
import fr.eyzox._new.configoption.events.Event;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public abstract class SingleValueFastConfigUpdater<T> extends FastConfigUpdater {

	public SingleValueFastConfigUpdater(ConfigOption<?> c) {
		super(c);
	}

	@Override
	public void applyChanges(FastConfig c, Event value) {
		if(value instanceof ChangedEvent<?>) {
			applyChanges(c, ((ChangedEvent<T>) value).getNewValue());
		}else {
			super.applyChanges(c, value);
		}
	}
	
	protected abstract void applyChanges(FastConfig c, T value);

}
