package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HealableDoorBlock extends HealableSupportByBottomBlock {

	private IBlockState blockstateUp;
	
	public HealableDoorBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		this.blockstateUp = world.getBlockState(pos.up());
	}

	@Override
	public void heal(World world, int flag) {
		super.heal(world, 2);
		BlockPos posUp = getPos().up();
		healBlock(world, posUp , blockstateUp, null, 2);
		world.notifyNeighborsOfStateChange(getPos(), this.getBlockState().getBlock());
		world.notifyNeighborsOfStateChange(posUp, this.getBlockState().getBlock());
	}
	
	

}
