package fr.eyzox.forgecreeperheal.healer.scheduler;

import java.util.Collection;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.RandomDependencyGraph;
import fr.eyzox.dependencygraph.interfaces.IDependencyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import net.minecraft.util.math.BlockPos;

public class BlockScheduler implements IScheduler<IBlockData> {

	private static final IDependencyProvider<BlockPos, IBlockData> provider = new DependencyProvider();
	
	private final RandomDependencyGraph<BlockPos, IBlockData> graph;
	
	public BlockScheduler(final Collection<? extends IBlockData> blocks) {
		this.graph = new RandomDependencyGraph<BlockPos, IBlockData>(blocks, provider);
	}

	@Override
	public IBlockData next() {
		return this.graph.poll();
	}

	@Override
	public boolean hasNext() {
		return this.graph.hasNext();
	}
	
	private static class DependencyProvider implements IDependencyProvider<BlockPos, IBlockData> {

		@Override
		public DependencyType<BlockPos, IBlockData> provideDependency(IBlockData data) {
			return ForgeCreeperHeal.getDependencyFactory().getData(data.getState().getBlock()).getDependencies(data);
		}
		
	}

}
