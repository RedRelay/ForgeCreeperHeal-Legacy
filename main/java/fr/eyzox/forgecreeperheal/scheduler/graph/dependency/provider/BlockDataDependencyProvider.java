package fr.eyzox.forgecreeperheal.scheduler.graph.dependency.provider;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.interfaces.IDependencyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import net.minecraft.util.math.BlockPos;

public class BlockDataDependencyProvider implements IDependencyProvider<BlockPos, IBlockData> {

	private final static BlockDataDependencyProvider INSTANCE = new BlockDataDependencyProvider();
	
	private BlockDataDependencyProvider() {
	}
	
	public static BlockDataDependencyProvider getInstance() {
		return INSTANCE;
	}
	
	@Override
	public DependencyType<BlockPos, IBlockData> provideDependency(IBlockData data) {
		return ForgeCreeperHeal.getDependencyFactory().getData(data.getState().getBlock()).getDependencies(data);
	}
	
}
