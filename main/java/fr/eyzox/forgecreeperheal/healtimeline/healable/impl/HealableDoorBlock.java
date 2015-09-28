package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.timeline.ICollector;

public class HealableDoorBlock extends HealableSupportByBottomBlock {

	private final BlockPos doorUpper;
	
	public HealableDoorBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		doorUpper = pos.up();
	}
	
	@Override
	public void collectKeys(ICollector collector) {
		super.collectKeys(collector);
		collector.collect(doorUpper);
	}

}
