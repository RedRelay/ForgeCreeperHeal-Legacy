package fr.eyzox.forgecreeperheal.healtimeline.factories;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;

public class FacingRequirementFactory extends PropertyRequirementFactory {
	
	public FacingRequirementFactory(IProperty property) {
		super(property);
	}

	public FacingRequirementFactory(Class<?> clazz, IProperty property) {
		super(clazz, property);
	}

	@Override
	public BlockPos[] getRequiredBlockPos(Key<BlockPos,BlockData> blockData, Enum e) {
		return getRequiredBlockPos(blockData, ((EnumFacing)e).getOpposite());
	}
	
	public static BlockPos[] getRequiredBlockPos(Key<BlockPos,BlockData> blockData, EnumFacing e) {
		return new BlockPos[]{blockData.getKey().offset(e)};
	}


}