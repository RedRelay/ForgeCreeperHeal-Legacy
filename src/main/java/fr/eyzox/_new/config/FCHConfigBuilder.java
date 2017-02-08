package fr.eyzox._new.config;

import fr.eyzox._new.config.MinMaxValidator.MinMaxGetter;

public class FCHConfigBuilder {

	public static final String OPTION_MIN_TICK_BEFORE_HEAL = "minTickStart";
	public static final String OPTION_MAX_TICK_BEFORE_HEAL = "maxTickStart";
	public static final String OPTION_MIN_TICK = "minTick";
	public static final String OPTION_MAX_TICK = "maxTick";
	public static final String OPTION_OVERRIDE_BLOCK = "overrideBlock";
	public static final String OPTION_OVERRIDE_FUILD = "overrideFluid";
	public static final String OPTION_DROP_IF_COLLISION = "dropIfCollision";
	public static final String OPTION_DROP_ITEMS = "dropItems";
	public static final String OPTION_REMOVE_EXCEPTION = "removeException";
	public static final String OPTION_HEAL_EXCEPTION = "healException";
	public static final String OPTION_SOURCE_EXCEPTION = "sourceException";
	
	ConfigProvider<JSONSerializer> configProvider;
	
	public void build() throws PropertyValidationException {
		final RestrictedConfigOption<Integer> minTickBeforeHeal = new RestrictedConfigOption<Integer>(OPTION_MIN_TICK_BEFORE_HEAL, 6000);
		final RestrictedConfigOption<Integer> maxTickBeforeStartHeal = new RestrictedConfigOption<Integer>(OPTION_MAX_TICK_BEFORE_HEAL, 12000);
		
		MinMaxGetter tickBeforeStartHealMinMaxGetter = new ConfigOptionMinMaxGetter(minTickBeforeHeal, maxTickBeforeStartHeal);
		
		minTickBeforeHeal.setValidator(new MinMaxValidator(tickBeforeStartHealMinMaxGetter) {
			@Override
			public boolean isValid(Integer obj) throws InvalidValueException {
				if(obj < 0) {
					throw new InvalidValueException(obj, minTickBeforeHeal.getName()+" must not be < 0");
				}
				return super.isValid(obj);
			}
		});
		
		maxTickBeforeStartHeal.setValidator(new MinMaxValidator(tickBeforeStartHealMinMaxGetter) {
			@Override
			public boolean isValid(Integer obj) throws InvalidValueException {
				if(obj < 0) {
					throw new InvalidValueException(obj, maxTickBeforeStartHeal.getName()+" must not be < 0");
				}
				return super.isValid(obj);
			}
		});
	}
	
	
	private class ConfigOptionMinMaxGetter extends MinMaxGetter {

		private final ConfigOption<Integer> min, max;
		
		public ConfigOptionMinMaxGetter(ConfigOption<Integer> min, ConfigOption<Integer> max) {
			super();
			this.min = min;
			this.max = max;
		}

		@Override
		public int getMin() {
			return min.getValue();
		}

		@Override
		public int getMax() {
			return max.getValue();
		}
		
	}
}
