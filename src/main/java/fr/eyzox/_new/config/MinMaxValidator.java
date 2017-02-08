package fr.eyzox._new.config;


public class MinMaxValidator implements IValidator<Integer> {
	
	public static abstract class MinMaxGetter {
		public abstract int getMin();
		public abstract int getMax();
		
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
	public boolean isValid(Integer obj) throws InvalidValueException {
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
