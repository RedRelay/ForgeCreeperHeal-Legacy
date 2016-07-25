package fr.eyzox.forgecreeperheal.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.eyzox.bsc.config.Config;
import fr.eyzox.bsc.config.ConfigOptionGroup;
import fr.eyzox.bsc.config.IConfigListener;
import fr.eyzox.bsc.config.option.ConfigOption;
import fr.eyzox.bsc.config.option.ConfigOptionList;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class FastConfig implements IConfigListener {

	private int minTickStart, maxTickStart;
	private int minTick, maxTick;
	
	private boolean overrideBlock, overrideFluid;
	private boolean dropIfCollision;
	
	private boolean dropItems;
	
	private final Set<String> removeException = new HashSet<String>();
	private final Set<String> healException = new HashSet<String>();
	private final Set<String> sourceException = new HashSet<String>();
	
	public FastConfig() {}

	@Override
	public void onChange(Config config) {
		
		removeException.clear();
		healException.clear();
		sourceException.clear();
		
		ConfigOptionGroup group = config.getOptionGroup(ConfigProvider.GROUP_HEALING_TIME);
		
		this.minTickStart = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MIN_TICK_BEFORE_HEAL)).getValue());
		this.maxTickStart = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MAX_TICK_BEFORE_HEAL)).getValue());
		
		this.minTick = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MIN_TICK)).getValue());
		this.maxTick = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MAX_TICK)).getValue());
		
		group = config.getOptionGroup(ConfigProvider.GROUP_OVERRIDE);
		
		this.overrideBlock = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_OVERRIDE_BLOCK)).getValue());
		this.overrideFluid = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_OVERRIDE_FUILD)).getValue());
		this.dropIfCollision = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_DROP_IF_COLLISION)).getValue());
		
		group = config.getOptionGroup(ConfigProvider.GROUP_CONTAINERS);
		
		this.dropItems = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_DROP_ITEMS)).getValue());
		
		group = config.getOptionGroup(ConfigProvider.GROUP_FILTERS);
		
		this.removeException.addAll(((ConfigOptionList)group.getOption(ConfigProvider.OPTION_REMOVE_EXCEPTION)).getValues());
		this.healException.addAll(((ConfigOptionList)group.getOption(ConfigProvider.OPTION_HEAL_EXCEPTION)).getValues());
		this.sourceException.addAll(((ConfigOptionList)group.getOption(ConfigProvider.OPTION_SOURCE_EXCEPTION)).getValues());

	}

	public int getMinTickStart() {
		return minTickStart;
	}

	public int getMaxTickStart() {
		return maxTickStart;
	}

	public int getMinTick() {
		return minTick;
	}

	public int getMaxTick() {
		return maxTick;
	}

	public boolean isOverrideBlock() {
		return overrideBlock;
	}

	public boolean isOverrideFluid() {
		return overrideFluid;
	}

	public boolean isDropIfCollision() {
		return dropIfCollision;
	}

	public boolean isDropItems() {
		return dropItems;
	}

	public Set<String> getRemoveException() {
		return removeException;
	}

	public Set<String> getHealException() {
		return healException;
	}

	public Set<String> getSourceException() {
		return sourceException;
	}
	
	

}
