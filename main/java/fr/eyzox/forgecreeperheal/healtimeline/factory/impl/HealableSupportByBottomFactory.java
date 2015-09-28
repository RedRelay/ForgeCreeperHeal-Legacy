package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableSupportByBottomBlock;

public class HealableSupportByBottomFactory extends AbstractHealableFactory {

	public HealableSupportByBottomFactory(Class<? extends Block> clazz) {
		super(clazz);
	}

	@Override
	public IHealableBlock create(World world, BlockPos pos, IBlockState state) {
		return new HealableSupportByBottomBlock(world, pos, state);
	}

}
