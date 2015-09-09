package fr.eyzox.forgecreeperheal.healtimeline;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockDataFactory {
	boolean accept(World world, BlockPos pos, IBlockState state);
	BlockData createBlockData(World world, BlockPos pos, IBlockState state);
}
