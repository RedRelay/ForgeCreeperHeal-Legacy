package fr.eyzox.forgecreeperheal.serial;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameData;

public class NBTUtils {

	private static final String TAG_IBLOCKSTATE_BLOCK = "block";
	private static final String TAG_IBLOCKSTATE_METADATA = "meta";
	
	public static NBTTagCompound iBlockStateToNBT(final IBlockState blockstate) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString(TAG_IBLOCKSTATE_BLOCK, GameData.getBlockRegistry().getNameForObject(blockstate.getBlock()).toString());
		final int metadata = blockstate.getBlock().getMetaFromState(blockstate);
		if(metadata != 0) {
			tag.setInteger(TAG_IBLOCKSTATE_METADATA, metadata);
		}
		return tag;
	}
	
	public static IBlockState iBlockStateFromNBT(final NBTTagCompound tag) {
		final Block block = Block.getBlockFromName(tag.getString(TAG_IBLOCKSTATE_BLOCK));
		final IBlockState state = block.getStateFromMeta(tag.getInteger(TAG_IBLOCKSTATE_METADATA));
		return state;
	}
	
}
