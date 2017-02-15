package fr.eyzox._new.fch;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import fr.eyzox._new.configoption.CollectionFactory;
import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.ConfigOptionCollection;
import fr.eyzox._new.configoption.ConfigOptionGroup;
import fr.eyzox._new.configoption.RestrictedConfigOption;
import fr.eyzox._new.configoption.events.AbstractCollectionChangedEvent;
import fr.eyzox._new.configoption.events.ClearedEvent;
import fr.eyzox._new.configoption.events.CollectionChangedEvent;
import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.IValidator;
import fr.eyzox._new.configoption.validator.MinMaxValidator;
import fr.eyzox._new.configoption.validator.MinMaxValidator.MinMaxGetter;
import fr.eyzox._new.configoption.validator.NotEmptyStringValidator;
import fr.eyzox._new.configoption.validator.NotNullValidator;
import fr.eyzox._new.fch.config.updaters.CollectionFastConfigUpdater;
import fr.eyzox._new.fch.config.updaters.FastConfigUpdater;
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

	private final ConfigOptionGroup configRoot;
	private final Map<ConfigOption<?>, FastConfigUpdater> fastConfigUpdaters = new HashMap<ConfigOption<?>, FastConfigUpdater>();

	private final IValidator<Integer> intNotNullValidator = new NotNullValidator<Integer>();
	private final IValidator<Boolean> booleanNotNullValidator = new NotNullValidator<Boolean>();
	private final IValidator<String> stringNotNullValidator = new NotEmptyStringValidator();

	public FCHConfigBuilder() throws PropertyValidationException {
		this.configRoot = new ConfigOptionGroup(ForgeCreeperHeal.MODID);
		configRoot.put(this.getHealingTimeGroup());
		configRoot.put(this.getOverrideGroup());
		configRoot.put(this.getContainersGroup());
		configRoot.put(this.getFiltersGroup());
	}

	private ConfigOptionGroup getHealingTimeGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_HEALING_TIME);
		final RestrictedConfigOption<Integer> minTickBeforeHeal = new RestrictedConfigOption<Integer>(OPTION_MIN_TICK_BEFORE_HEAL, 6000);
		final RestrictedConfigOption<Integer> maxTickBeforeHeal = new RestrictedConfigOption<Integer>(OPTION_MAX_TICK_BEFORE_HEAL, 12000);

		minTickBeforeHeal.setValidator(new MinMaxValidator(new MinMaxGetter() {
			public int getMin() {
				return 0;
			}

			public int getMax() {
				return maxTickBeforeHeal.getValue();
			}
		}));

		maxTickBeforeHeal.setValidator(new MinMaxValidator(new MinMaxGetter() {
			public int getMin() {
				return minTickBeforeHeal.getValue();
			}
		}));

		final RestrictedConfigOption<Integer> minTickBetweenEachHeal = new RestrictedConfigOption<Integer>(OPTION_MIN_TICK, 0);
		final RestrictedConfigOption<Integer> maxTickBetweenEachHeal = new RestrictedConfigOption<Integer>(OPTION_MAX_TICK, 200);

		minTickBetweenEachHeal.setValidator(new MinMaxValidator(new MinMaxGetter() {
			public int getMin() {
				return 0;
			}

			public int getMax() {
				return maxTickBetweenEachHeal.getValue();
			}
		}));

		maxTickBetweenEachHeal.setValidator(new MinMaxValidator(new MinMaxGetter() {
			public int getMin() {
				return minTickBetweenEachHeal.getValue();
			}
		}));

		g.put(minTickBeforeHeal);
		g.put(maxTickBeforeHeal);
		g.put(minTickBetweenEachHeal);
		g.put(maxTickBetweenEachHeal);

		fastConfigUpdaters.put(minTickBeforeHeal, new SingleValueFastConfigUpdater<Integer>() {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMinTickStart(value);
			}
		});
		fastConfigUpdaters.put(maxTickBeforeHeal, new SingleValueFastConfigUpdater<Integer>() {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMaxTickStart(value);
			}
		});
		fastConfigUpdaters.put(minTickBetweenEachHeal, new SingleValueFastConfigUpdater<Integer>() {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMinTick(value);
			}
		});
		fastConfigUpdaters.put(maxTickBetweenEachHeal, new SingleValueFastConfigUpdater<Integer>() {
			@Override
			public void applyChanges(FastConfig c, Integer value) {
				c.setMaxTick(value);
			}
		});

		return g;

	}

	private ConfigOptionGroup getOverrideGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_OVERRIDE);

		final ConfigOption<Boolean> overrideBlock = new ConfigOption<Boolean>(OPTION_OVERRIDE_BLOCK, false);
		final ConfigOption<Boolean> overrideFluid = new ConfigOption<Boolean>(OPTION_OVERRIDE_FUILD, true);
		final ConfigOption<Boolean> dropIfCollision = new ConfigOption<Boolean>(OPTION_DROP_IF_COLLISION, true);

		g.put(overrideBlock);
		g.put(overrideFluid);
		g.put(dropIfCollision);

		fastConfigUpdaters.put(overrideBlock, new SingleValueFastConfigUpdater<Boolean>() {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setOverrideBlock(value);
			}
		});
		fastConfigUpdaters.put(overrideFluid, new SingleValueFastConfigUpdater<Boolean>() {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setOverrideFluid(value);
			}
		});
		fastConfigUpdaters.put(dropIfCollision, new SingleValueFastConfigUpdater<Boolean>() {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setDropIfCollision(value);
			}
		});

		return g;
	}

	private ConfigOptionGroup getContainersGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_CONTAINERS);

		final ConfigOption<Boolean> dropItems = new ConfigOption<Boolean>(OPTION_DROP_ITEMS, false);

		g.put(dropItems);

		fastConfigUpdaters.put(dropItems, new SingleValueFastConfigUpdater<Boolean>() {
			@Override
			public void applyChanges(FastConfig c, Boolean value) {
				c.setDropItems(value);
			}
		});

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

		final ConfigOptionCollection<String> removeException = new ConfigOptionCollection<String>(OPTION_REMOVE_EXCEPTION, factory);
		final ConfigOptionCollection<String> healException = new ConfigOptionCollection<String>(OPTION_HEAL_EXCEPTION, factory);
		final ConfigOptionCollection<String> sourceException = new ConfigOptionCollection<String>(OPTION_SOURCE_EXCEPTION, factory);

		g.put(removeException);
		g.put(healException);
		g.put(sourceException);


		fastConfigUpdaters.put(removeException, new CollectionFastConfigUpdater<String>() {
			@Override
			protected Collection<String> getCollection(FastConfig c) {
				return c.getRemoveException();
			}
		});

		fastConfigUpdaters.put(healException, new CollectionFastConfigUpdater<String>() {
			@Override
			protected Collection<String> getCollection(FastConfig c) {
				return c.getHealException();
			}
		});

		fastConfigUpdaters.put(sourceException, new CollectionFastConfigUpdater<String>() {
			@Override
			protected Collection<String> getCollection(FastConfig c) {
				return c.getSourceException();
			}
		});


		return g;
	}

	public ConfigOptionGroup getConfigRoot() {
	    return this.configRoot;
    }

	public Map<ConfigOption<?>, FastConfigUpdater> getFastConfigUpdaters() {
		return fastConfigUpdaters;
	}
}
