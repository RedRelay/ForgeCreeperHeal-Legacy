package fr.eyzox.forgecreeperheal.healtimeline.factories;

import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.forgecreeperheal.healtimeline.HealTimeline;
import fr.eyzox.forgecreeperheal.healtimeline.requirementcheck.IRequirementChecker;

public abstract class ClassRequirementFactory implements IRequirementFactory {

	private Class<?> clazz;

	public ClassRequirementFactory(Class<?> clazz) {
		this.clazz = clazz;
	}
	

	@Override
	public IRequirementChecker getRequirementBase(BlockData blockData) {
		return HealTimeline.getOnlyOneDependenceRequierement();
	}

	@Override
	public boolean accept(BlockData data) {
		return clazz.isAssignableFrom(data.getBlockState().getBlock().getClass());
	}
	
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
