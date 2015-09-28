package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.factory.IHealableFactory;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableBlock;

public class HealableDefaultFactory implements IHealableFactory {

	@Override
	public IHealableBlock create(World world, BlockPos pos, IBlockState state) {
		if(!(state.getBlock().isAir(world, pos) || ForgeCreeperHeal.getConfig().getHealException().contains(state.getBlock()))) {
			return new HealableBlock(world, pos, state);
		}
		return null;
	}

	@Override
	public boolean accept(Class<? extends Block> clazz) {
		return true;
	}
	

}
