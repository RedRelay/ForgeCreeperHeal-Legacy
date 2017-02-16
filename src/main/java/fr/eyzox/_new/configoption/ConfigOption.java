package fr.eyzox._new.configoption;

import java.util.Observable;

import fr.eyzox._new.configoption.events.ChangedEvent;
import fr.eyzox._new.configoption.events.Event;

public class ConfigOption<T> extends Observable{
	
	private transient boolean fireEvent = true;
	
	private transient final String name;
	private T value;
	
	
	public ConfigOption(String name, T defaultValue) {
		this.name = name;
		this.value = defaultValue;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		final T oldValue = this.value;
		this.value = value;
		this.fireEvent(new ChangedEvent<T>(this, oldValue, this.value));
	}

	public String getName() {
		return name;
	}

	protected void fireEvent(Event event) {
		if(fireEvent) {
			fireEvent = false;
			this.setChanged();
			this.notifyObservers(event);
			fireEvent = true;
		}
	}

}
