package fr.eyzox._new.configoption;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eyzox._new.configoption.exceptions.InvalidValueException;
import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.IValidator;

public class RestrictedConfigOptionCollection<T> extends ConfigOptionCollection<T> implements IRestricted<T> {

	private static final Logger logger = LogManager.getLogger(RestrictedConfigOptionCollection.class);
	
	private transient IValidator<T> validator;
	
	public <C extends Collection<T>> RestrictedConfigOptionCollection(String name, CollectionFactory<C, T> factory) {
		super(name, factory);
	}
	
	public <C extends Collection<T>> RestrictedConfigOptionCollection(String name, CollectionFactory<C, T> factory, C defaultValue) {
		super(name, factory, defaultValue);
	}

	public <C extends Collection<T>> RestrictedConfigOptionCollection(String name, CollectionFactory<C, T> factory, C defaultValue, IValidator<T> validator) throws PropertyValidationException {
		this(name, factory, defaultValue);
		setValidator(validator);
	}
	
	public <C extends Collection<T>> RestrictedConfigOptionCollection(String name, CollectionFactory<C, T> factory, IValidator<T> validator) throws PropertyValidationException {
		this(name, factory);
		setValidator(validator);
	}

	@Override
	public void setValidator(IValidator<T> validator) throws PropertyValidationException {
		this.validator = validator;
		if(validator != null) {
			for(T element : getValue()) {
				try {
					if(!validator.isValid(this, element)) {
						throw new InvalidValueException(element);
					}
				} catch (InvalidValueException e) {
					throw new PropertyValidationException(this, e);
				}
			}
		}
	}

	@Override
	public IValidator<T> getValidator() {
		return validator;
	}

	@Override
	public boolean add(T element) throws PropertyValidationException {
		try {
			if(validator != null && !validator.isValid(this, element)) {
				return super.add(element);
			}
			throw new InvalidValueException(element);
		} catch (InvalidValueException e) {
			throw new PropertyValidationException(this, e);
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean b = true;
		for(T element : c) {
			try {
				if(!add(element)) {
					b = false;
				}
			} catch (PropertyValidationException e) {
				logger.catching(e);
			}
		}
		return b;
	}
	
	

}
