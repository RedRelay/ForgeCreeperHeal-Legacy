package fr.eyzox._new.config;

import fr.eyzox._new.config.MinMaxValidator.MinMaxGetter;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.List;

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

		final ConfigOption<Boolean> overrideBlock = new ConfigOption<Boolean>(OPTION_OVERRIDE_BLOCK, false);
		final ConfigOption<Boolean> overrideFluid = new ConfigOption<Boolean>(OPTION_OVERRIDE_FUILD, true);

		final ConfigOption<Boolean> dropIfCollision = new ConfigOption<Boolean>(OPTION_DROP_IF_COLLISION, true);

		final ConfigOption<Boolean> dropItems = new ConfigOption<Boolean>(OPTION_DROP_ITEMS, false);

		final ConfigOption<List<String>> removeException = new ConfigOption<List<String>>(OPTION_REMOVE_EXCEPTION, new ArrayList<String>());
		final ConfigOption<List<String>> healException = new ConfigOption<List<String>>(OPTION_HEAL_EXCEPTION, new ArrayList<String>());
		final ConfigOption<List<String>> sourceException = new ConfigOption<List<String>>(OPTION_SOURCE_EXCEPTION, new ArrayList<String>());

		removeException.getValue().add(Blocks.TNT.getRegistryName().toString());
		healException.getValue().add(Blocks.TNT.getRegistryName().toString());

	}

}
