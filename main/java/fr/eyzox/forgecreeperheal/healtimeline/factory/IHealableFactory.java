package fr.eyzox.forgecreeperheal.healtimeline.factory;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;

public interface IHealableFactory {
	boolean accept(Class<? extends Block> clazz);
	IHealableBlock create(World world, BlockPos pos, IBlockState state);
}
