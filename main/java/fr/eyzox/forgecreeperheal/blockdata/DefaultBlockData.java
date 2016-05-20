package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healer.HealerUtils;
import fr.eyzox.forgecreeperheal.serial.NBTUtils;
import fr.eyzox.forgecreeperheal.serial.wrapper.BlockSerialWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultBlockData implements IBlockData{

	private static final String TAG_POS = "pos";
	private static final String TAG_STATE = "state";
	
	private BlockPos pos;
	private IBlockState state;
	
	private final BlockSerialWrapper wrapper = new BlockSerialWrapper();
	
	public DefaultBlockData(final BlockPos pos, final IBlockState state) {
		this.pos = pos;
		this.state = state;
		this.wrapper.initialize(ForgeCreeperHeal.getProxy().getBlockClassKeyBuilder().convertToString(this.state.getBlock().getClass()));
	}
	
	public DefaultBlockData(final NBTTagCompound tag) {
		this.readFromNBT(tag);
		this.wrapper.initialize(ForgeCreeperHeal.getProxy().getBlockClassKeyBuilder().convertToString(this.state.getBlock().getClass()));
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
	public void writeToNBT(NBTTagCompound tag) {
		tag.setLong(TAG_POS, this.pos.toLong());
		tag.setTag(TAG_STATE, NBTUtils.iBlockStateToNBT(this.state));
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		this.pos = BlockPos.fromLong(tag.getLong(TAG_POS));
		this.state = NBTUtils.iBlockStateFromNBT(tag.getCompoundTag(TAG_STATE));
	}

	@Override
	public BlockSerialWrapper getSerialWrapper() {
		return this.wrapper;
	}
	
	

}
