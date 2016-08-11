package fr.eyzox.forgecreeperheal.scheduler.graph.dependency.provider;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.interfaces.IDependencyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import net.minecraft.util.BlockPos;

public class BlockDataDependencyProvider implements IDependencyProvider<BlockPos, BlockData> {

	private final static BlockDataDependencyProvider INSTANCE = new BlockDataDependencyProvider();
	
	private BlockDataDependencyProvider() {
	}
	
	public static BlockDataDependencyProvider getInstance() {
		return INSTANCE;
	}
	
	@Override
	public DependencyType<BlockPos, BlockData> provideDependency(BlockData data) {
		return ForgeCreeperHeal.getDependencyFactory().getData(data.getState().getBlock()).getDependencies(data);
	}
	
}
