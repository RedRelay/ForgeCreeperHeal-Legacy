package fr.eyzox._new.configoption.validator;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public class MinMaxValidator<T extends Comparable<T>> extends NotNullValidator<T> {
	
	public static abstract class MinMaxGetter<T extends Comparable<T>> {
		public abstract T getMin();
		public abstract T getMax();
		
		private void validate() throws InvalidValueException {
			if(getMin().compareTo(getMax()) > 0) {
				throw new InvalidValueException(String.format("Min >= Max (%d >= %d)", getMin(), getMax()));
			}
		}
	}

	private final MinMaxGetter<T> minMaxGetter;

	public MinMaxValidator(MinMaxGetter<T> mmg) {
		this.minMaxGetter = mmg;
	}

	@Override
	public boolean isValid(ConfigOption<?> config, T obj) throws InvalidValueException {
		if(super.isValid(config, obj)) {
			minMaxGetter.validate();
			if(obj.compareTo(minMaxGetter.getMin()) < 0) {
				throw new InvalidValueException(obj, "value must be > "+minMaxGetter.getMin());
			}
			if(obj.compareTo(minMaxGetter.getMax()) > 0) {
				throw new InvalidValueException(obj, "value must be < "+minMaxGetter.getMax());
			}
			return true;
		}
		return false;
	}
}
