package fr.eyzox._new.configoption;

import fr.eyzox._new.configoption.events.ChangedEvent;
import fr.eyzox._new.configoption.events.IEvent;

import java.util.Observable;

public class ConfigOption<T> extends Observable{
	private final String name;
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
		this.fireEvent(new ChangedEvent<T>(oldValue, this.value));
	}

	public String getName() {
		return name;
	}

	protected void fireEvent(IEvent event) {
		this.setChanged();
		this.notifyObservers(event);
	}

}
