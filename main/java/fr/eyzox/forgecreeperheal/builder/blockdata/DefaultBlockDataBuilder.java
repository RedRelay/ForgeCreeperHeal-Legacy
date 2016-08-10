package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class DefaultBlockDataBuilder implements IBlockDataBuilder {

	public DefaultBlockDataBuilder() {}

	@Override
	public BlockData create(World world, BlockPos pos, IBlockState state) {
		if(state.getBlock().isAir(world, pos)) return null;
		return new BlockData(pos, state);
	}

	@Override
	public BlockData create(NBTTagCompound tag) {
		return new BlockData(tag);
	}
	
	@Override
	public boolean accept(Block block) {
		return true;
	}
}
