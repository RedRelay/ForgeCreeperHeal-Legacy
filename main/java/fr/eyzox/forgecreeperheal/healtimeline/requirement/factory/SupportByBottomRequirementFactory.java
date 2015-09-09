package fr.eyzox.forgecreeperheal.healtimeline.requirement.factory;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;

public class SupportByBottomRequirementFactory extends BlockClassRequirementFactory {

	public SupportByBottomRequirementFactory(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public BlockPos[] getKeyDependencies(Key<BlockPos,BlockData> blockData) {
		return new BlockPos[]{FacingRequirementFactory.getRequiredBlockPos(blockData, EnumFacing.DOWN)};
	}

}
