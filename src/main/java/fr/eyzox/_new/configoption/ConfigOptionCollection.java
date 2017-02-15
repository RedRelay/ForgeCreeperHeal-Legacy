package fr.eyzox._new.configoption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import fr.eyzox._new.configoption.events.AbstractCollectionChangedEvent.State;
import fr.eyzox._new.configoption.events.ClearedEvent;
import fr.eyzox._new.configoption.events.CollectionChangedEvent;

public class ConfigOptionCollection<T> extends ConfigOption<Collection<T>> {
	
	private CollectionFactory<?, T> factory;
	
	public <C extends Collection<T>> ConfigOptionCollection(String name, CollectionFactory<C, T> factory) {
		this(name, factory, factory.create());
	}
	
	public <C extends Collection<T>> ConfigOptionCollection(String name, CollectionFactory<C, T> factory, C defaultValue) {
		super(name, defaultValue == null ? new ArrayList<T>() : defaultValue);
		this.factory = factory;
	}

	@Override
	public Collection<T> getValue() {
		return Collections.unmodifiableCollection(super.getValue());
	}

	@Override
	public void setValue(Collection<T> value) {
		final Collection<T> newValues = factory.create();
		newValues.addAll(value);
		super.setValue(newValues);
		
	}
	
	public void clear() {
		final Collection<T> oldValues = factory.create();
		oldValues.addAll(super.getValue());
		super.getValue().clear();
		this.fireEvent(new ClearedEvent<Collection<T>>(this, oldValues, super.getValue()));
	}
	
	public <C extends Collection<T>> void changeCollectionImpl(CollectionFactory<C, T> factory) {
		this.factory = factory;
		Collection<T> c = factory.create();
		c.addAll(super.getValue());
		super.setValue(c);
	}

	public boolean add(T e) {
		final boolean added = super.getValue().add(e);
		if(added) {
			this.fireEvent(new CollectionChangedEvent<T>(this, super.getValue(), e, State.ADDED));
		}
		return added;
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean hasChanged = false;
		for(T e : c) {
			if(this.add(e)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean remove(T o) {
		final boolean removed = super.getValue().remove(o);
		if(removed) {
			this.fireEvent(new CollectionChangedEvent<T>(this, super.getValue(), o, State.REMOVED));
		}
		return removed;
	}

	public boolean removeAll(Collection<? extends T> c) {
		boolean hasChanged = false;
		for(T e : c) {
			if(this.remove(e)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	

}
