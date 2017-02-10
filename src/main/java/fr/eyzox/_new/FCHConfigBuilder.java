package fr.eyzox._new;

import java.util.ArrayList;
import java.util.List;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.ConfigOptionGroup;
import fr.eyzox._new.configoption.RestrictedConfigOption;
import fr.eyzox._new.configoption.exceptions.PropertyValidationException;
import fr.eyzox._new.configoption.validator.MinMaxValidator;
import fr.eyzox._new.configoption.validator.MinMaxValidator.MinMaxGetter;
import fr.eyzox._new.fch.IFastConfigUpdater;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

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
	
	public ConfigOptionGroup build() throws PropertyValidationException {

		final ConfigOptionGroup r = new ConfigOptionGroup(ForgeCreeperHeal.MODID);
		r.add(this.getHealingTimeGroup());
		r.add(this.getOverrideGroup());
		r.add(this.getContainersGroup());
		r.add(this.getFiltersGroup());

		return r;
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

		g.add(minTickBeforeHeal);
		g.add(maxTickBeforeHeal);
		g.add(minTickBetweenEachHeal);
		g.add(maxTickBetweenEachHeal);

		return g;

	}

	private ConfigOptionGroup getOverrideGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_OVERRIDE);

		final ConfigOption<Boolean> overrideBlock = new ConfigOption<Boolean>(OPTION_OVERRIDE_BLOCK, false);
		final ConfigOption<Boolean> overrideFluid = new ConfigOption<Boolean>(OPTION_OVERRIDE_FUILD, true);
		final ConfigOption<Boolean> dropIfCollision = new ConfigOption<Boolean>(OPTION_DROP_IF_COLLISION, true);

		g.add(overrideBlock);
		g.add(overrideFluid);
		g.add(dropIfCollision);

		return g;
	}

	private ConfigOptionGroup getContainersGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_CONTAINERS);

		final ConfigOption<Boolean> dropItems = new ConfigOption<Boolean>(OPTION_DROP_ITEMS, false);

		g.add(dropItems);

		return g;
	}

	private ConfigOptionGroup getFiltersGroup() throws PropertyValidationException {
		final ConfigOptionGroup g = new ConfigOptionGroup(GROUP_FILTERS);

		final ConfigOption<List<String>> removeException = new ConfigOption<List<String>>(OPTION_REMOVE_EXCEPTION, new ArrayList<String>());
		final ConfigOption<List<String>> healException = new ConfigOption<List<String>>(OPTION_HEAL_EXCEPTION, new ArrayList<String>());
		final ConfigOption<List<String>> sourceException = new ConfigOption<List<String>>(OPTION_SOURCE_EXCEPTION, new ArrayList<String>());

		g.add(removeException);
		g.add(healException);
		g.add(sourceException);

		return g;
	}
}
