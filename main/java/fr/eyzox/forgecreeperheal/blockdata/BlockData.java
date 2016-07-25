package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.dependencygraph.DataKeyProvider;
import fr.eyzox.dependencygraph.SingleDataKeyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealerSerialException;
import fr.eyzox.forgecreeperheal.factory.Factory;
import fr.eyzox.forgecreeperheal.healer.HealerUtils;
import fr.eyzox.forgecreeperheal.healer.IChunked;
import fr.eyzox.forgecreeperheal.healer.IHealable;
import fr.eyzox.forgecreeperheal.healer.IRemovable;
import fr.eyzox.forgecreeperheal.healer.WorldRemover;
import fr.eyzox.forgecreeperheal.serial.ISerialWrapper;
import fr.eyzox.forgecreeperheal.serial.ISerialWrapperProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;

public class BlockData implements fr.eyzox.dependencygraph.interfaces.IData<BlockPos>, ISerialWrapperProvider<BlockData>, IChunked, IHealable, IRemovable, INBTSerializable<NBTTagCompound>{

	protected static final String TAG_POS = "pos";
	protected static final String TAG_STATE = "state";
	
	protected static final String TAG_IBLOCKSTATE_BLOCK = "block";
	protected static final String TAG_IBLOCKSTATE_METADATA = "meta";
	
	protected static final String TAG_TILE_ENTITY = "tileEntity";
	
	private BlockPos pos;
	private IBlockState state;
	private NBTTagCompound tileEntity;
	
	public BlockData(final BlockPos pos, final IBlockState state) {
		this(pos, state, null); 
	}
	
	public BlockData(final BlockPos pos, final IBlockState state, final NBTTagCompound tileEntity) {
		this.pos = pos;
		this.state = state;
		this.tileEntity = tileEntity;
	}
	
	public BlockData(final NBTTagCompound tag) {
		this.deserializeNBT(tag);
	}
	
	public final BlockPos getPos() {
		return pos;
	}
	
	public final IBlockState getState() {
		return state;
	}
	
	public final NBTTagCompound getTileEntity() {
		return tileEntity;
	}
	
	public boolean isMultiple() {
		return false;
	}
	
	public static boolean hasTileEntity(final NBTTagCompound tag) {
		return tag.hasKey(TAG_TILE_ENTITY, NBT.TAG_COMPOUND);
	}
	
	@Override
	public void heal(World world, int flags) {
		HealerUtils.healBlock(world, pos, state, null, flags);
	}
	
	@Override
	public void remove(WorldRemover remover) {
		remover.remove(pos);
	}

	@Override
	public DataKeyProvider<BlockPos> getDataKeyProvider() {
		return new SingleDataKeyProvider<BlockPos>(pos);
	}

	@Override
	public final int getChunkX() {
		return pos.getX() >> 4;
	}
	
	@Override
	public final int getChunkZ() {
		return pos.getZ() >> 4;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag =  new NBTTagCompound();
		tag.setLong(TAG_POS, this.pos.toLong());
		tag.setTag(TAG_STATE, iBlockStateToNBT(this.state));
		
		if(tileEntity != null) {
			tag.setTag(TAG_TILE_ENTITY, tileEntity);
		}
		
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		this.pos = BlockPos.fromLong(tag.getLong(TAG_POS));
		this.state = iBlockStateFromNBT(tag.getCompoundTag(TAG_STATE));
		
		final NBTTagCompound tileEntityTag = tag.getCompoundTag(TAG_TILE_ENTITY);
		this.tileEntity = tileEntityTag.hasNoTags() ? null : tileEntityTag;
	}

	@Override
	public final BlockDataSerialWrapper getSerialWrapper() {
		return BlockDataSerialWrapper.getInstance();
	}
	
	private NBTTagCompound iBlockStateToNBT(final IBlockState blockstate) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setString(TAG_IBLOCKSTATE_BLOCK, blockstate.getBlock().getRegistryName().toString());
		final int metadata = blockstate.getBlock().getMetaFromState(blockstate);
		if(metadata != 0) {
			tag.setInteger(TAG_IBLOCKSTATE_METADATA, metadata);
		}
		return tag;
	}
	
	private IBlockState iBlockStateFromNBT(final NBTTagCompound tag) {
		final Block block = Block.getBlockFromName((tag.getString(TAG_IBLOCKSTATE_BLOCK)));
		final IBlockState state = block.getStateFromMeta(tag.getInteger(TAG_IBLOCKSTATE_METADATA));
		return state;
	}
	
	public static class BlockDataSerialWrapper implements ISerialWrapper<BlockData>{
		
		private static final BlockDataSerialWrapper INSTANCE = new BlockDataSerialWrapper();
		
		public static BlockDataSerialWrapper getInstance() { return INSTANCE;}
		
		private BlockDataSerialWrapper() {}

		@Override
		public BlockData unserialize(NBTTagCompound tag) throws ForgeCreeperHealerSerialException{
			final Factory<Block, IBlockDataBuilder> factory = ForgeCreeperHeal.getBlockDataFactory();
			final String blockName = getBlockName(tag);
			
			if(blockName.isEmpty()) {
				throw new ForgeCreeperHealerSerialException("Missing Block's name");
			}
			
			return factory.getData(blockName).create(tag);
		}

		@Override
		public NBTTagCompound serialize(BlockData data) {
			return data.serializeNBT();
		}
		
		private String getBlockName(NBTTagCompound blockDataTag) {
			final NBTTagCompound blockStateTag = blockDataTag.getCompoundTag((BlockData.TAG_STATE));
			return blockStateTag.getString(BlockData.TAG_IBLOCKSTATE_BLOCK);
		}

	}
	
}
