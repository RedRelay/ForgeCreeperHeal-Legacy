package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableLeverBlock;

public class HealableLeverFactory extends AbstractHealableFactory {

	public HealableLeverFactory() {
		super(BlockLever.class);
	}

	@Override
	public IHealableBlock create(World world, BlockPos pos, IBlockState state) {
		return new HealableLeverBlock(world, pos, state);
	}

}
