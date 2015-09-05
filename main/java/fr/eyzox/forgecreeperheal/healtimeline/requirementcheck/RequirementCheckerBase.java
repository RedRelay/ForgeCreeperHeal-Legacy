package fr.eyzox.forgecreeperheal.healtimeline.requirementcheck;

import java.util.Collection;

import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import net.minecraft.util.BlockPos;


public abstract class RequirementCheckerBase implements IRequirementChecker{
	public boolean canBePlaced(Collection<BlockPos> blockPosCollection, BlockData data) {
		if(blockPosCollection == null) return true;
		return _canBePlaced(blockPosCollection, data);
	}
	
	protected abstract boolean _canBePlaced(Collection<BlockPos> blockPosCollection, BlockData data);
}
