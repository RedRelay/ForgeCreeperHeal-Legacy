package fr.eyzox._new.configoption.validator;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public class MinMaxValidator implements IValidator<Integer> {
	
	public static class MinMaxGetter {
		public int getMin() {
			return Integer.MIN_VALUE;
		}
		public int getMax() {
			return Integer.MAX_VALUE;
		}
		
		private void validate() throws IllegalArgumentException {
			if(getMin() >= getMax()) {
				throw new IllegalArgumentException(String.format("Min >= Max (%d >= %d)", getMin(), getMax()));
			}
		}
	}

	private final MinMaxGetter minMaxGetter;

	public MinMaxValidator(MinMaxGetter mmg) {
		this.minMaxGetter = mmg;
	}

	@Override
	public boolean isValid(ConfigOption<?> config, Integer obj) throws InvalidValueException {
		minMaxGetter.validate();
		if(obj < minMaxGetter.getMin()) {
			throw new InvalidValueException(obj, "value must be > "+minMaxGetter.getMin());
		}
		if(obj > minMaxGetter.getMax()) {
			throw new InvalidValueException(obj, "value must be < "+minMaxGetter.getMax());
		}
		return true;
	}

}
