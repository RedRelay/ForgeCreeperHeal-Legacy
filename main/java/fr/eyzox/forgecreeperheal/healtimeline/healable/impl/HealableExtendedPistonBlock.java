package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.timeline.ICollector;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableExtendedPistonBlock extends HealableBlock {

	private final IHealableBlock extendedPiston;
	
	public HealableExtendedPistonBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		final BlockPos extendedPistonPos = pos.offset((EnumFacing) state.getValue(BlockPistonBase.FACING));
		extendedPiston = new HealableBlock(world, extendedPistonPos, world.getBlockState(extendedPistonPos));
	}
	
	@Override
	public void collectKeys(ICollector collector) {
		super.collectKeys(collector);
		extendedPiston.collectKeys(collector);
	}
	
	@Override
	public void heal(World world, int flags) {
		super.heal(world, flags);
		extendedPiston.heal(world, flags);
	}
	

}
