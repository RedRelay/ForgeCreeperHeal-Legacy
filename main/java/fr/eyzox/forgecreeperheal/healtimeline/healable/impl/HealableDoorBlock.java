package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HealableDoorBlock extends HealableMultiBlock {

	
	public HealableDoorBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		final BlockPos posUp = pos.up();
		this.getLinkedHealables().put(posUp, world.getBlockState(posUp));
	}

	@Override
	public Object[] getDependencies() {
		return new BlockPos[]{HealableSupportByBottomBlock.getDependencies(this.getPos())};
	}
	
	

}
