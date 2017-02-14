package fr.eyzox.forgecreeperheal.config;

import java.util.HashSet;
import java.util.Set;

public class FastConfig {

	private int minTickStart, maxTickStart;
	private int minTick, maxTick;
	
	private boolean overrideBlock, overrideFluid;
	private boolean dropIfCollision;
	
	private boolean dropItems;
	
	private final Set<String> removeException = new HashSet<String>();
	private final Set<String> healException = new HashSet<String>();
	private final Set<String> sourceException = new HashSet<String>();

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
}
