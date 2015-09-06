package fr.eyzox.forgecreeperheal.healtimeline.factories;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;

public class SupportByBottomRequirementFactory extends ClassRequirementFactory {

	public SupportByBottomRequirementFactory(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public BlockPos[] getRequiredBlockPos(BlockData blockData) {
		return FacingRequirementFactory.getRequiredBlockPos(blockData, EnumFacing.DOWN);
	}

}
