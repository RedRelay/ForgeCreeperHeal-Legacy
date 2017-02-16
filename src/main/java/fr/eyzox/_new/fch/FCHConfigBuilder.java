package fr.eyzox._new.fch;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import fr.eyzox._new.configoption.CollectionFactory;
import fr.eyzox._new.configoption.ConfigOptionGroup;
import fr.eyzox._new.configoption.RestrictedConfigOption;
import fr.eyzox._new.configoption.RestrictedConfigOptionCollection;
import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.IValidator;
import fr.eyzox._new.configoption.validator.MinMaxValidator;
import fr.eyzox._new.configoption.validator.MinMaxValidator.MinMaxGetter;
import fr.eyzox._new.configoption.validator.NotEmptyStringValidator;
import fr.eyzox._new.configoption.validator.NotNullValidator;
import fr.eyzox._new.fch.config.updaters.CollectionFastConfigUpdater;
import fr.eyzox._new.fch.config.updaters.SingleValueFastConfigUpdater;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.config.FastConfig;

/**
 * Build a default config tree
 */
public class FCHConfigBuilder {
	
	public static final String GROUP_HEALING_TIME = "healing time";
	public static final String GROUP_OVERRIDE = "override";
	public static final String GROUP_CONTAINERS = "containers";
	public static final String GROUP_FILTERS = "filters";

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

	private ConfigOptionGroup configRoot;

	private final IValidator<Integer> intNotNullValidator = new NotNullValidator<Integer>();
	private final IValidator<Boolean> booleanNotNullValidator = new NotNullValidator<Boolean>();
	private final IValidator<String> stringNotEmptyValidator = new NotEmptyStringValidator();
	
	public synchronized ConfigOptionGroup build() throws PropertyValidationException{
		if(configRoot == null) {
			this.configRoot = new ConfigOptionGroup(ForgeCreeperHeal.MODID);
			configRoot.put(this.getHealingTimeGroup());
			configRoot.put(this.getOverrideGroup());
			configRoot.put(this.getContainersGroup());
			configRoot.put(this.getFiltersGroup());
		}
		return configRoot;
	}
	
	public synchronized void unbuild() {
		this.configRoot = null;
	}

	private ConfigOptionGroup getHealingTimeGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_HEALING_TIME);
		final RestrictedConfigOption<Integer> minTickBeforeHeal = new RestrictedConfigOption<Integer>(OPTION_MIN_TICK_BEFORE_HEAL, 6000);
		final RestrictedConfigOption<Integer> maxTickBeforeHeal = new RestrictedConfigOption<Integer>(OPTION_MAX_TICK_BEFORE_HEAL, 12000);
		final RestrictedConfigOption<Integer> minTickBetweenEachHeal = new RestrictedConfigOption<Integer>(OPTION_MIN_TICK, 0);
		final RestrictedConfigOption<Integer> maxTickBetweenEachHeal = new RestrictedConfigOption<Integer>(OPTION_MAX_TICK, 200);
		
		minTickBeforeHeal.setValidator(new MinMaxValidator<Integer>(new MinMaxGetter<Integer>() {
			public Integer getMin() {
				return 0;
			}

			public Integer getMax() {
				return maxTickBeforeHeal.getValue();
			}
		}));

		maxTickBeforeHeal.setValidator(new MinMaxValidator<Integer>(new MinMaxGetter<Integer>() {
			public Integer getMin() {
				return minTickBeforeHeal.getValue();
			}

			@Override
			public Integer getMax() {
				return Integer.MAX_VALUE;
			}
		}));

		minTickBetweenEachHeal.setValidator(new MinMaxValidator<Integer>(new MinMaxGetter<Integer>() {
			public Integer getMin() {
				return 0;
			}

			public Integer getMax() {
				return maxTickBetweenEachHeal.getValue();
			}
		}));

		maxTickBetweenEachHeal.setValidator(new MinMaxValidator<Integer>(new MinMaxGetter<Integer>() {
			public Integer getMin() {
				return minTickBetweenEachHeal.getValue();
			}

			@Override
			public Integer getMax() {
				return Integer.MAX_VALUE;
			}
		}));

		g.put(minTickBeforeHeal);
		g.put(maxTickBeforeHeal);
		g.put(minTickBetweenEachHeal);
		g.put(maxTickBetweenEachHeal);

		new SingleValueFastConfigUpdater<Integer>(minTickBeforeHeal) {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMinTickStart(value);
			}
		};
		
		new SingleValueFastConfigUpdater<Integer>(maxTickBeforeHeal) {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMaxTickStart(value);
			}
		};
		new SingleValueFastConfigUpdater<Integer>(minTickBetweenEachHeal) {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMinTick(value);
			}
		};
		new SingleValueFastConfigUpdater<Integer>(maxTickBetweenEachHeal) {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMaxTick(value);
			}
		};

		return g;

	}

	private ConfigOptionGroup getOverrideGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_OVERRIDE);

		final RestrictedConfigOption<Boolean> overrideBlock = new RestrictedConfigOption<Boolean>(OPTION_OVERRIDE_BLOCK, false, booleanNotNullValidator);
		final RestrictedConfigOption<Boolean> overrideFluid = new RestrictedConfigOption<Boolean>(OPTION_OVERRIDE_FUILD, true, booleanNotNullValidator);
		final RestrictedConfigOption<Boolean> dropIfCollision = new RestrictedConfigOption<Boolean>(OPTION_DROP_IF_COLLISION, true, booleanNotNullValidator);

		g.put(overrideBlock);
		g.put(overrideFluid);
		g.put(dropIfCollision);

		new SingleValueFastConfigUpdater<Boolean>(overrideBlock) {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setOverrideBlock(value);
			}
		};
		new SingleValueFastConfigUpdater<Boolean>(overrideFluid) {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setOverrideFluid(value);
			}
		};
		new SingleValueFastConfigUpdater<Boolean>(dropIfCollision) {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setDropIfCollision(value);
			}
		};

		return g;
	}

	private ConfigOptionGroup getContainersGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_CONTAINERS);

		final RestrictedConfigOption<Boolean> dropItems = new RestrictedConfigOption<Boolean>(OPTION_DROP_ITEMS, false, booleanNotNullValidator);

		g.put(dropItems);

		new SingleValueFastConfigUpdater<Boolean>(dropItems) {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setDropItems(value);
			}
		};

		return g;
	}

	private ConfigOptionGroup getFiltersGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_FILTERS);
		
		final CollectionFactory<Collection<String>, String> factory = new CollectionFactory<Collection<String>, String>() {
			@Override
			public Collection<String> create() {
				return new HashSet<String>();
			}
		}; 

		final RestrictedConfigOptionCollection<String> removeException = new RestrictedConfigOptionCollection<String>(OPTION_REMOVE_EXCEPTION, factory, stringNotEmptyValidator);
		final RestrictedConfigOptionCollection<String> healException = new RestrictedConfigOptionCollection<String>(OPTION_HEAL_EXCEPTION, factory, stringNotEmptyValidator);
		final RestrictedConfigOptionCollection<String> sourceException = new RestrictedConfigOptionCollection<String>(OPTION_SOURCE_EXCEPTION, factory, stringNotEmptyValidator);

		g.put(removeException);
		g.put(healException);
		g.put(sourceException);


		new CollectionFastConfigUpdater<String>(removeException) {
			@Override
			protected Collection<String> getCollection(FastConfig c) {
				return c.getRemoveException();
			}
		};

		new CollectionFastConfigUpdater<String>(healException) {
			@Override
			protected Collection<String> getCollection(FastConfig c) {
				return c.getHealException();
			}
		};

		new CollectionFastConfigUpdater<String>(sourceException) {
			@Override
			protected Collection<String> getCollection(FastConfig c) {
				return c.getSourceException();
			}
		};


		return g;
	}

	public void save(File file) {

	}

	public void load(File file) {

	}
}
