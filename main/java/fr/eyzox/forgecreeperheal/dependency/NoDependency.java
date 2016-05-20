package fr.eyzox.forgecreeperheal.dependency;

import java.util.Set;

import net.minecraft.util.math.BlockPos;

public class NoDependency extends DefaultDependency {

	public NoDependency() {
		super(new BlockPos[0]);
	}
	
	@Override
	public boolean isAvailable(Set<BlockPos> dependenciesLeft) {
		return true;
	}

}
