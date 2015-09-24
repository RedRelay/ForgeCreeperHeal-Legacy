package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import fr.eyzox.timeline.ICollector;

public class HealableSupportByBottomBlock extends HealableBlock {

	public HealableSupportByBottomBlock(World world, BlockPos pos,IBlockState state) {
		super(world, pos, state);
	}
	
	
	@Override
	public void collectDependenciesKeys(ICollector<Object> collector) {
		collector.collect(getDependencies(this.getPos()));
		super.collectDependenciesKeys(collector);
	}

	public static BlockPos getDependencies(BlockPos blockPos) {
		return HealableFacingBlock.getRequiredBlockPos(blockPos, EnumFacing.DOWN);
	}
}
