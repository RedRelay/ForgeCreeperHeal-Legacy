package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableVineBlock;

public class HealableVineFactory extends AbstractHealableFactory {

	public HealableVineFactory() {
		super(BlockVine.class);
	}

	@Override
	public IHealableBlock create(World world, BlockPos pos, IBlockState state) {
		return new HealableVineBlock(world, pos, state);
	}

}
