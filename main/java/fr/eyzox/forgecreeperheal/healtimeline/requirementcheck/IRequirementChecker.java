package fr.eyzox.forgecreeperheal.healtimeline.requirementcheck;

import java.util.Collection;

import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import net.minecraft.util.BlockPos;

public interface IRequirementChecker {
	public boolean canBePlaced(Collection<BlockPos> blockPosCollection, BlockData data);
}
