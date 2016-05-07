package fr.eyzox.forgecreeperheal.healer.scheduler;

import java.util.Collection;

import fr.eyzox.dependencygraph.DependencyGraph;
import fr.eyzox.dependencygraph.IDependency;
import fr.eyzox.dependencygraph.IDependencyProvider;
import fr.eyzox.dependencygraph.RandomDependencyGraph;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import net.minecraft.util.BlockPos;

public class BlockScheduler implements IScheduler<IBlockData> {

	private static final IDependencyProvider<BlockPos, IBlockData> provider = new DependencyProvider();
	
	private final DependencyGraph<BlockPos, IBlockData> graph;
	
	public BlockScheduler(final Collection<? extends IBlockData> blocks) {
		this.graph = new RandomDependencyGraph<BlockPos, IBlockData>(blocks, provider);
	}

	@Override
	public IBlockData next() {
		return this.graph.next();
	}

	@Override
	public boolean hasNext() {
		return this.graph.hasNext();
	}
	
	private static class DependencyProvider implements IDependencyProvider<BlockPos, IBlockData> {

		@Override
		public IDependency<BlockPos> provideDependency(IBlockData data) {
			return ForgeCreeperHeal.getDependencyFactory().getData(data.getState().getBlock().getClass()).getDependencies(data);
		}
		
	}

}
