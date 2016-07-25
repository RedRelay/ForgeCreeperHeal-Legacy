package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.forgecreeperheal.healer.IChunked;
import fr.eyzox.forgecreeperheal.healer.IHealable;
import fr.eyzox.forgecreeperheal.healer.IRemovable;
import fr.eyzox.forgecreeperheal.serial.ISerialWrapperProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBlockData extends fr.eyzox.dependencygraph.interfaces.IData<BlockPos>, ISerialWrapperProvider, IChunked, IHealable, IRemovable, INBTSerializable<NBTTagCompound> {
	public BlockPos getPos();
	public IBlockState getState();
}
