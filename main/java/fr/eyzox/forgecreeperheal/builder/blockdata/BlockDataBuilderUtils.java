package fr.eyzox.forgecreeperheal.builder.blockdata;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDataBuilderUtils {

	private BlockDataBuilderUtils() {}
	
	public static NBTTagCompound getTileEntityNBT(final World world, final BlockPos pos, final IBlockState state) {
		NBTTagCompound tileEntityTag = null;
		if(state.getBlock().hasTileEntity(state)) {
			tileEntityTag = new NBTTagCompound();
			world.getTileEntity(pos).writeToNBT(tileEntityTag);
		}
		return tileEntityTag;
	}

}
