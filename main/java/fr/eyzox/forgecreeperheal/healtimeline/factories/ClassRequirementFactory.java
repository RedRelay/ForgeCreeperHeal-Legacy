package fr.eyzox.forgecreeperheal.healtimeline.factories;

import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;

public abstract class ClassRequirementFactory implements IRequirementFactory {

	@Override
	public BlockPos[] getRequiredBlockPos(BlockData blockData) {
		if(clazz.isAssignableFrom(blockData.getBlockState().getBlock().getClass())) {
			return getRequiredBlockPosForThisClass(blockData);
		}
		return null;
	}

	private Class<?> clazz;
	
	public ClassRequirementFactory(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	protected abstract BlockPos[] getRequiredBlockPosForThisClass(BlockData blockData);

}
