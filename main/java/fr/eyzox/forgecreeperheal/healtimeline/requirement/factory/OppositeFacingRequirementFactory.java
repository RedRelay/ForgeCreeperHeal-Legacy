package fr.eyzox.forgecreeperheal.healtimeline.requirement.factory;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;

public class OppositeFacingRequirementFactory extends PropertyRequirementFactory{

	public OppositeFacingRequirementFactory(IProperty property) {
		super(property);
	}

	public OppositeFacingRequirementFactory(Class<?> clazz, IProperty property) {
		super(clazz, property);
	}

	@Override
	public BlockPos[] getRequiredBlockPos(Key<BlockPos,BlockData> blockData, Enum e) {
		return new BlockPos[]{getRequiredBlockPos(blockData, (EnumFacing)e)};
	}
	
	public static BlockPos getRequiredBlockPos(Key<BlockPos,BlockData> blockData, EnumFacing e) {
		return blockData.getKey().offset(e);
	}
}
