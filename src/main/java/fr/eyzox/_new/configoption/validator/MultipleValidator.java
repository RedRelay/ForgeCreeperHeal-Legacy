package fr.eyzox._new.configoption.validator;

import java.util.ArrayList;
import java.util.List;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public class MultipleValidator<T> implements IValidator<T> {

	private final List<IValidator<T>> validatorsChain;
	
	public MultipleValidator() {
		this(new ArrayList<IValidator<T>>());
	}

	public MultipleValidator(List<IValidator<T>> validatorsChain) {
		this.validatorsChain = validatorsChain;
	}

	@Override
	public boolean isValid(ConfigOption<?> config, T obj) throws InvalidValueException {
		for(IValidator<T> validator : validatorsChain) {
			if(!validator.isValid(config, obj)) {
				return false;
			}
		}
		return true;
	}

	public List<IValidator<T>> getValidatorsChain() {
		return validatorsChain;
	}

}
