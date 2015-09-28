package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import fr.eyzox.timeline.ICollector;
import fr.eyzox.timeline.IDependencyChecker;

public class HealableBedBlock extends HealableBlock {

	private final BlockPos footPos;
	
	public HealableBedBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		footPos = pos.offset(((EnumFacing) state.getValue(BlockBed.FACING)).getOpposite());
	}
	
	@Override
	public void collectDependenciesKeys(ICollector collector) {
		collector.collect(HealableSupportByBottomBlock.getDependencies(this.getPos()));
		collector.collect(HealableSupportByBottomBlock.getDependencies(this.footPos));
		super.collectDependenciesKeys(collector);
	}
	
	@Override
	public void collectKeys(ICollector collector) {
		super.collectKeys(collector);
		collector.collect(footPos);
	}
	
	@Override
	public boolean isAvailable(IDependencyChecker checker) {
		return !(checker.isStillRequired(getPos()) || checker.isStillRequired(footPos));
	}

}
