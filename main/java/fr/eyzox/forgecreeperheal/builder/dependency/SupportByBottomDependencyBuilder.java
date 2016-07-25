package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.SingleDependency;
import fr.eyzox.dependencygraph.interfaces.IDependencyUpdater;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.dependency.FullAndDependency;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SupportByBottomDependencyBuilder extends AbstractGenericDependencyBuilder {

	private final static SupportByBottomDependencyBuilder INSTANCE = new SupportByBottomDependencyBuilder();
	private SupportByBottomDependencyBuilder() {}

	@Override
	public DependencyType<BlockPos, IBlockData> getDependencies(IBlockData data) {
		return new SingleDependency(FacingDependencyUtils.getBlockPos(data.getPos(), EnumFacing.DOWN));
	}
	
	public static SupportByBottomDependencyBuilder getInstance() {
		return INSTANCE;
	}

}
