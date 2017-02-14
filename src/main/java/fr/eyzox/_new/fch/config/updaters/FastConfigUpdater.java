package fr.eyzox._new.fch.config.updaters;

import java.util.Observable;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.events.IEvent;
import fr.eyzox._new.fch.IFastConfigUpdater;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public abstract class FastConfigUpdater implements Observer, IFastConfigUpdater{

	private final static Logger LOGGER = LogManager.getLogger(FastConfigUpdater.class);

	@Override
	public void update(Observable obs, Object o) {
		if(obs instanceof ConfigOption<?> && o instanceof IEvent) {
			final ConfigOption<?> config = (ConfigOption<?>) obs;
			final IEvent event = (IEvent) o;
			this.applyChanges(this.getFastConfig(), event);
		}else {
			LOGGER.warn(String.format("Unexpected %s (%s) or %s (%s)",ConfigOption.class.getSimpleName(), obs, IEvent.class.getSimpleName(), o)); 
		}
	}
	
	protected void onUnexpectedEvent(IEvent event) {
		LOGGER.warn(String.format("[%s] Unexpected %s (%s)", this.getClass().getSimpleName(), IEvent.class.getSimpleName(), event));
	}
	
	private FastConfig getFastConfig() {
		return null;
	}

}
