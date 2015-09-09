package fr.eyzox.forgecreeperheal.healtimeline.requirement.factory;

import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;
import fr.eyzox.timeline.factory.ClassRequirementFactory;

public abstract class BlockClassRequirementFactory extends ClassRequirementFactory<BlockPos, BlockData> {

	public BlockClassRequirementFactory(Class clazz) {
		super(clazz);
	}

	@Override
	public Class getCheckedClass(Key<BlockPos, BlockData> data) {
		return data.getValue().getBlockState().getBlock().getClass();
	}

	

}
