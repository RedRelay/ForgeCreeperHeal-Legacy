package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableExtendedPistonBlock extends HealableMultiBlock {

	public HealableExtendedPistonBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		BlockPos extendedPistonPos = pos.offset((EnumFacing) state.getValue(BlockPistonBase.FACING));
		getLinkedHealables().put(extendedPistonPos, world.getBlockState(extendedPistonPos));
	}

}
