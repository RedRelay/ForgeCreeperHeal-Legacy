package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HealableMultiBlock extends HealableBlock {

	private final Map<BlockPos, IBlockState> linkedHealables = new HashMap<BlockPos, IBlockState>();

	public HealableMultiBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
	}

	public Map<BlockPos, IBlockState> getLinkedHealables() {
		return linkedHealables;
	}

	@Override
	public void heal(World world, int flag) {
		super.heal(world, 2);

		for(Entry<BlockPos, IBlockState> entry : linkedHealables.entrySet()) {
			healBlock(world, entry.getKey() , entry.getValue(), null, 2);
		}

		world.notifyNeighborsOfStateChange(getPos(), this.getBlockState().getBlock());

		if((flag & 1) != 0) {
			for(Entry<BlockPos, IBlockState> entry : linkedHealables.entrySet()) {
				world.notifyNeighborsOfStateChange(entry.getKey(), entry.getValue().getBlock());
			}
		}
	}

	@Override
	public void removeFromWorld(World world) {
		super.removeFromWorld(world);
		for(BlockPos pos : linkedHealables.keySet()) {
			removeFromWorld(world, pos);
		}
	}
	
	

}
