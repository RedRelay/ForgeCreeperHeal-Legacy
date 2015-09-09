package fr.eyzox.forgecreeperheal.healtimeline.requirement.factory;

import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;

public class LeverRequirementFactory extends PropertyRequirementFactory {

	public LeverRequirementFactory() {
		super(BlockLever.class, BlockLever.FACING);
	}

	@Override
	public BlockPos[] getRequiredBlockPos(Key<BlockPos,BlockData> blockData, Enum e) {
		return new BlockPos[]{FacingRequirementFactory.getRequiredBlockPos(blockData, ((EnumOrientation)e).getFacing())};
	}


}
