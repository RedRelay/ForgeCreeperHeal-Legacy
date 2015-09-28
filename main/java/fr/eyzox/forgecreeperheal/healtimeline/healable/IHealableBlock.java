package fr.eyzox.forgecreeperheal.healtimeline.healable;

import fr.eyzox.timeline.ICollector;
import fr.eyzox.timeline.ITimelineElement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public interface IHealableBlock extends IHealable, ITimelineElement{
	IBlockState getBlockState();
	NBTTagCompound getTileEntityTag();
	BlockPos getPos();
	void collectBlockPos(ICollector<BlockPos> collector);
}
