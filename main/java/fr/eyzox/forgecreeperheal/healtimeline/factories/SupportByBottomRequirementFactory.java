package fr.eyzox.forgecreeperheal.healtimeline.factories;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.forgecreeperheal.healtimeline.requirementcheck.IRequirementChecker;

public class SupportByBottomRequirementFactory extends ClassRequirementFactory {

	public SupportByBottomRequirementFactory(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public IRequirementChecker getRequirementBase(BlockData blockData) {
		return null;
	}

	@Override
	protected BlockPos[] getRequiredBlockPosForThisClass(BlockData blockData) {
		return FacingRequirementFactory.getRequiredBlockPos(blockData, EnumFacing.DOWN);
	}

}
