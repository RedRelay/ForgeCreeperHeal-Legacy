package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.timeline.ICollector;

public class HealableDoorBlock extends HealableMultiBlock {

	
	public HealableDoorBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		final BlockPos posUp = pos.up();
		this.getLinkedHealables().put(posUp, world.getBlockState(posUp));
	}
	
	@Override
	public void collectDependenciesKeys(ICollector<Object> collector) {
		collector.collect(HealableSupportByBottomBlock.getDependencies(this.getPos()));
		super.collectDependenciesKeys(collector);
	}

}
