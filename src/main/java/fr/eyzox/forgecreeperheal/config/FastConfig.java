package fr.eyzox.forgecreeperheal.config;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.fch.IFastConfigUpdater;
import fr.eyzox.bsc.config.IConfigListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FastConfig implements IConfigListener {

	private final Map<ConfigOption<?>, IFastConfigUpdater<?>> updaters;

	private int minTickStart, maxTickStart;
	private int minTick, maxTick;
	
	private boolean overrideBlock, overrideFluid;
	private boolean dropIfCollision;
	
	private boolean dropItems;
	
	private final Set<String> removeException = new HashSet<String>();
	private final Set<String> healException = new HashSet<String>();
	private final Set<String> sourceException = new HashSet<String>();

	public FastConfig() {
		this(new HashMap<ConfigOption<?>, IFastConfigUpdater<?>>());
	}
	
	public FastConfig(Map<ConfigOption<?>, IFastConfigUpdater<?>> updaters) {
		this.updaters = updaters;
	}

	public int getMinTickStart() {
		return minTickStart;
	}

	public void setMinTickStart(int minTickStart) {
		this.minTickStart = minTickStart;
	}

	public int getMaxTickStart() {
		return maxTickStart;
	}

	public void setMaxTickStart(int maxTickStart) {
		this.maxTickStart = maxTickStart;
	}

	public int getMinTick() {
		return minTick;
	}

	public void setMinTick(int minTick) {
		this.minTick = minTick;
	}

	public int getMaxTick() {
		return maxTick;
	}

	public void setMaxTick(int maxTick) {
		this.maxTick = maxTick;
	}

	public boolean isOverrideBlock() {
		return overrideBlock;
	}

	public void setOverrideBlock(boolean overrideBlock) {
		this.overrideBlock = overrideBlock;
	}

	public boolean isOverrideFluid() {
		return overrideFluid;
	}

	public void setOverrideFluid(boolean overrideFluid) {
		this.overrideFluid = overrideFluid;
	}

	public boolean isDropIfCollision() {
		return dropIfCollision;
	}

	public void setDropIfCollision(boolean dropIfCollision) {
		this.dropIfCollision = dropIfCollision;
	}

	public boolean isDropItems() {
		return dropItems;
	}

	public void setDropItems(boolean dropItems) {
		this.dropItems = dropItems;
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

	public Map<ConfigOption<?>, IFastConfigUpdater<?>> getUpdaters() {
		return updaters;
	}

	@Override
	public <T> void onChange(ConfigOption<T> config) {
		IFastConfigUpdater<T> updater = (IFastConfigUpdater<T>) updaters.get(config);
		if(updater != null) {
			updater.applyChanges(this, config.getValue());
		}
	}
}
