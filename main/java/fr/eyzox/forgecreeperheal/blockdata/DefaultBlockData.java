package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.dependencygraph.DataKeyProvider;
import fr.eyzox.dependencygraph.SingleDataKeyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealerSerialException;
import fr.eyzox.forgecreeperheal.factory.Factory;
import fr.eyzox.forgecreeperheal.factory.keybuilder.BlockKeyBuilder;
import fr.eyzox.forgecreeperheal.healer.HealerUtils;
import fr.eyzox.forgecreeperheal.serial.ISerialWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class DefaultBlockData implements IBlockData{

	public static final String TAG_POS = "pos";
	public static final String TAG_STATE = "state";
	public static final String TAG_IBLOCKSTATE_BLOCK = "block";
	public static final String TAG_IBLOCKSTATE_METADATA = "meta";
	
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
	public BlockPos getPos() {
		return pos;
	}
	
	@Override
	public DataKeyProvider<BlockPos> getDataKeyProvider() {
		return new SingleDataKeyProvider<BlockPos>(pos);
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
		
		if(!tag.hasKey(TAG_POS, NBT.TAG_LONG)) {
			throw new ForgeCreeperHealerSerialException("Missing BlockPos data");
		}
		
		if(!tag.hasKey(TAG_STATE, NBT.TAG_COMPOUND)) {
			throw new ForgeCreeperHealerSerialException("Missing BlockState data");
		}
		
		this.pos = BlockPos.fromLong(tag.getLong(TAG_POS));
		this.state = iBlockStateFromNBT(tag.getCompoundTag(TAG_STATE));
	}

	@Override
	public BlockDataSerialWrapper getSerialWrapper() {
		return BlockDataSerialWrapper.getInstance();
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
		
		if(!tag.hasKey(TAG_IBLOCKSTATE_BLOCK, NBT.TAG_STRING)) {
			throw new ForgeCreeperHealerSerialException("Missing Block's name");
		}
		
		final Block block = BlockKeyBuilder.getInstance().convertToKey(tag.getString(TAG_IBLOCKSTATE_BLOCK));
		final IBlockState state = block.getStateFromMeta(tag.getInteger(TAG_IBLOCKSTATE_METADATA));
		return state;
	}
	
	public static class BlockDataSerialWrapper implements ISerialWrapper<DefaultBlockData>{
		
		private static final BlockDataSerialWrapper INSTANCE = new BlockDataSerialWrapper();
		
		public static BlockDataSerialWrapper getInstance() { return INSTANCE;}
		
		private BlockDataSerialWrapper() {}

		@Override
		public DefaultBlockData unserialize(NBTTagCompound tag) throws ForgeCreeperHealerSerialException{
			final Factory<Block, IBlockDataBuilder> factory = ForgeCreeperHeal.getBlockDataFactory();
			final String blockName = getBlockName(tag);
			
			if(blockName.isEmpty()) {
				throw new ForgeCreeperHealerSerialException("Missing Block's name");
			}
			
			return (DefaultBlockData) factory.getData(blockName).create(tag);
		}

		@Override
		public NBTTagCompound serialize(DefaultBlockData data) {
			return data.serializeNBT();
		}
		
		private String getBlockName(NBTTagCompound blockDataTag) {
			final NBTTagCompound blockStateTag = blockDataTag.getCompoundTag(DefaultBlockData.TAG_STATE);
			return blockStateTag.getString(DefaultBlockData.TAG_IBLOCKSTATE_BLOCK);
		}

	}

}
