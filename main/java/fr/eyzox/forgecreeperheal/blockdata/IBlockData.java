package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IBlockData extends fr.eyzox.dependencygraph.IData<BlockPos>, ISerializableHealable {
	public BlockPos getPos();
	public BlockPos[] getAllPos();
	public IBlockState getState();
}
