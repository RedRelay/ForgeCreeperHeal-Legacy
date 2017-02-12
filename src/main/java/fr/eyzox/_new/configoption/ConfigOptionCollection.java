package fr.eyzox._new.configoption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ConfigOptionCollection<T> extends ConfigOption<Collection<T>> {
	
	public ConfigOptionCollection(String name) {
		this(name, null);
	}
	
	public ConfigOptionCollection(String name, Collection<T> defaultValue) {
		super(name, defaultValue == null ? new ArrayList<T>() : defaultValue);
	}

	@Override
	public Collection<T> getValue() {
		return Collections.unmodifiableCollection(super.getValue());
	}

	@Override
	public void setValue(Collection<T> value) {
		super.getValue().clear();
		if(value != null) {
			this.addAll(value);
		}
	}
	
	public void clear() {
		super.getValue().clear();
	}
	
	public void changeCollectionImpl(CollectionFactory<T> factory) {
		Collection<T> c = factory.create();
		c.addAll(super.getValue());
		super.setValue(c);
	}

	public boolean add(T e) {
		return super.getValue().add(e);
	}

	public boolean addAll(Collection<? extends T> c) {
		return super.getValue().addAll(c);
	}

	public boolean remove(Object o) {
		return super.getValue().remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return super.getValue().removeAll(c);
	}
	
	

}
