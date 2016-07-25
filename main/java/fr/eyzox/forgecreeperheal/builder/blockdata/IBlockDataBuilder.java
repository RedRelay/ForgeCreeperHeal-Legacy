package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.factory.IData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockDataBuilder extends IData<Block> {
	public IBlockData create(final World w, final BlockPos pos, final IBlockState state);
	public IBlockData create(final NBTTagCompound tag);
}
