package fr.eyzox.forgecreeperheal.healtimeline.requirement.factory;

import net.minecraft.block.BlockVine;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;

public class VineRequirementFactory extends BlockClassRequirementFactory{ 
	
	public VineRequirementFactory() {
		super(BlockVine.class);
	}

	@Override
	public BlockPos[] getKeyDependencies(Key<BlockPos, BlockData> data) {
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			if(((Boolean)data.getValue().getBlockState().getValue(BlockVine.getPropertyFor(facing))).booleanValue()) {
				return new BlockPos[]{FacingRequirementFactory.getRequiredBlockPos(data, facing),FacingRequirementFactory.getRequiredBlockPos(data,EnumFacing.UP)};
			}
		}
		
		return null;
	}

}
