package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.factory.Factory;
import fr.eyzox.forgecreeperheal.factory.keybuilder.BlockKeyBuilder;
import fr.eyzox.forgecreeperheal.healer.HealerUtils;
import fr.eyzox.forgecreeperheal.serial.ISerialWrapper;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class DefaultBlockData implements IBlockData{

	public static final String TAG_POS = "pos";
	public static final String TAG_STATE = "state";
	public static final String TAG_IBLOCKSTATE_BLOCK = "block";
	public static final String TAG_IBLOCKSTATE_METADATA = "meta";
	
	private static final BlockDataSerialWrapper wrapper = new BlockDataSerialWrapper();
	
	private BlockPos pos;
	private IBlockState state;
	
	
	public DefaultBlockData(final BlockPos pos, final IBlockState state) {
		this.pos = pos;
		this.state = state;
	}
	
	public DefaultBlockData(final NBTTagCompound tag) {
		this.deserializeNBT(tag);
	}
	
	@Override
	public void heal(World world, int flags) {
		HealerUtils.healBlock(world, pos, state, null, flags);
	}
	
	@Override
	public BlockPos[] getKeys() {
		return getAllPos();
	}

	@Override
	public BlockPos getPos() {
		return pos;
	}
	
	@Override
	public BlockPos[] getAllPos() {
		return new BlockPos[]{pos};
	}

	@Override
	public IBlockState getState() {
		return state;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag =  new NBTTagCompound();
		tag.setLong(TAG_POS, this.pos.toLong());
		tag.setTag(TAG_STATE, iBlockStateToNBT(this.state));
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		this.pos = BlockPos.fromLong(tag.getLong(TAG_POS));
		this.state = iBlockStateFromNBT(tag.getCompoundTag(TAG_STATE));
	}

	@Override
	public BlockDataSerialWrapper getSerialWrapper() {
		return this.wrapper;
	}
	
	protected static NBTTagCompound iBlockStateToNBT(final IBlockState blockstate) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString(TAG_IBLOCKSTATE_BLOCK, BlockKeyBuilder.getInstance().convertToString(blockstate.getBlock()));
		final int metadata = blockstate.getBlock().getMetaFromState(blockstate);
		if(metadata != 0) {
			tag.setInteger(TAG_IBLOCKSTATE_METADATA, metadata);
		}
		return tag;
	}
	
	protected static IBlockState iBlockStateFromNBT(final NBTTagCompound tag) {
		final Block block = BlockKeyBuilder.getInstance().convertToKey(tag.getString(TAG_IBLOCKSTATE_BLOCK));
		final IBlockState state = block.getStateFromMeta(tag.getInteger(TAG_IBLOCKSTATE_METADATA));
		return state;
	}
	
	public static class BlockDataSerialWrapper implements ISerialWrapper<ISerializableHealable>{
		
		public BlockDataSerialWrapper() {}

		@Override
		public ISerializableHealable unserialize(NBTTagCompound tag) {
			final Factory<Block, IBlockDataBuilder> factory = ForgeCreeperHeal.getBlockDataFactory();
			final String blockName = getBlockName(tag);
			return factory.getData(blockName).create(tag);
		}

		@Override
		public NBTTagCompound serialize(ISerializableHealable data) {
			return data.serializeNBT();
		}
		
		private String getBlockName(NBTTagCompound blockDataTag) {
			final NBTTagCompound blockStateTag = blockDataTag.getCompoundTag(DefaultBlockData.TAG_STATE);
			return blockStateTag.getString(DefaultBlockData.TAG_IBLOCKSTATE_BLOCK);
		}

	}

}
