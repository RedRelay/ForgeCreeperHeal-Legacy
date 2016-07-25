package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.SingleDependency;
import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import net.minecraft.util.EnumFacing;

public class SupportByBottomDependencyBuilder extends AbstractGenericDependencyBuilder {

	private final static SupportByBottomDependencyBuilder INSTANCE = new SupportByBottomDependencyBuilder();
	private SupportByBottomDependencyBuilder() {}

	@Override
	public DependencyType<BlockPos, BlockData> getDependencies(BlockData data) {
		return new SingleDependency(FacingDependencyUtils.getBlockPos(data.getPos(), EnumFacing.DOWN));
	}
	
	public static SupportByBottomDependencyBuilder getInstance() {
		return INSTANCE;
	}

}
