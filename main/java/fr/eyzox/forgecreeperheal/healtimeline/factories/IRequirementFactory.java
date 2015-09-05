package fr.eyzox.forgecreeperheal.healtimeline.factories;

import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.forgecreeperheal.healtimeline.requirementcheck.IRequirementChecker;

public interface IRequirementFactory {
	public BlockPos[] getRequiredBlockPos(BlockData blockData);
	public IRequirementChecker getRequirementBase(BlockData blockData);
}
