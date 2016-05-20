package fr.eyzox.forgecreeperheal.dependency;

import fr.eyzox.dependencygraph.IDependency;
import net.minecraft.util.math.BlockPos;

/**
 * All dependencies have to be released to be available
 * @author EyZox
 *
 */
public abstract class DefaultDependency implements IDependency<BlockPos> {

	private final BlockPos[] dependencies;
	
	public DefaultDependency(final BlockPos[] dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public BlockPos[] getDependencies() {
		return dependencies;
	}

	

}
