package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.factory.IHealableFactory;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableExtendedPistonBlock;

public class HealablePistonFactory implements IHealableFactory {

	@Override
	public boolean accept(Class<? extends Block> clazz) {
		return BlockPistonBase.class.isAssignableFrom(clazz) || BlockPistonExtension.class.isAssignableFrom(clazz) || BlockPistonMoving.class.isAssignableFrom(clazz);
	}

	@Override
	public IHealable create(World world, BlockPos pos, IBlockState state) {
		
		if(BlockPistonBase.class.isAssignableFrom(state.getBlock().getClass())) {
			
			if( ((Boolean)state.getValue(BlockPistonBase.EXTENDED)).booleanValue() ) {
				return new HealableExtendedPistonBlock(world, pos, state);
			}
			
			return new HealableBlock(world, pos, state);
			
		}
		
		return null;
		
	}



}
