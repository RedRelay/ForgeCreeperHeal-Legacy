package fr.eyzox._new.fch.config.updaters;

import java.util.Observable;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.events.Event;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.config.FastConfig;

public abstract class FastConfigUpdater implements Observer{

	private final static Logger LOGGER = LogManager.getLogger(FastConfigUpdater.class);

	public FastConfigUpdater(ConfigOption<?> c) {
		c.addObserver(this);
	}
	
	@Override
	public void update(Observable obs, Object o) {
		if(obs instanceof ConfigOption<?> && o instanceof Event) {
			final ConfigOption<?> config = (ConfigOption<?>) obs;
			final Event event = (Event) o;
			this.applyChanges(this.getFastConfig(), event);
		}else {
			LOGGER.warn(String.format("Unexpected %s (%s) or %s (%s)",ConfigOption.class.getSimpleName(), obs, Event.class.getSimpleName(), o)); 
		}
	}

	public void applyChanges(FastConfig c, Event value) {
		onUnexpectedEvent(value);
	}

	protected void onUnexpectedEvent(Event event) {
		LOGGER.warn(String.format("[%s] Unexpected %s (%s)", this.getClass().getSimpleName(), Event.class.getSimpleName(), event));
	}
	
	private FastConfig getFastConfig() {
		return ForgeCreeperHeal.getConfig();
	}

}
