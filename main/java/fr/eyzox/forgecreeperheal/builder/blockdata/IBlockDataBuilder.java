package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.factory.IData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockDataBuilder extends IData<Block> {
	public BlockData create(final World w, final BlockPos pos, final IBlockState state);
	public BlockData create(final NBTTagCompound tag);
}
