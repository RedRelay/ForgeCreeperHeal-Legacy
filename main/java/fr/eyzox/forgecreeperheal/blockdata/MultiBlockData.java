package fr.eyzox.forgecreeperheal.blockdata;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * Warning : Not recursive ! A MultiBlockData can't handle other MultiBlockData !
 * @author EyZox
 *
 */
public class MultiBlockData extends TileEntityBlockData {

	private final static String TAG_WRAPPERS = "wrappers";
	
	private final static String TAG_WRAPPER_FACTORY_KEY = "key";
	private final static String TAG_WRAPPER_DATA = "data";
	
	
	private IBlockData[] others;
	
	public MultiBlockData(BlockPos pos, IBlockState state, NBTTagCompound tileEntityTag, final IBlockData[] others) {
		super(pos, state, tileEntityTag);
		this.others = others;
	}
	
	public MultiBlockData(final NBTTagCompound tag) {
		super(tag);
	}

	@Override
	public BlockPos[] getAllPos() {
		final BlockPos[] allPos = new BlockPos[1+others.length];
		allPos[0] = getPos();
		for(int i = 0; i<others.length; i++) {
			allPos[i+1] = others[i].getPos();
		}
		return allPos;
	}
	
	@Override
	public void heal(World world, int flags) {
		
		IBlockState[] oldStates = new IBlockState[1+others.length];
		
		for(int i = 0; i<others.length; i++) {
			oldStates[i] = world.getBlockState(others[i].getPos());
			others[i].heal(world, 0);
		}
		
		oldStates[oldStates.length-1] = world.getBlockState(getPos());
		super.heal(world, 0);
		
		for(int i = 0; i<others.length; i++) {
			world.markAndNotifyBlock(others[i].getPos(), world.getChunkFromBlockCoords(others[i].getPos()), oldStates[i], others[i].getState(), flags);
		}
		world.markAndNotifyBlock(getPos(), world.getChunkFromBlockCoords(getPos()), oldStates[oldStates.length-1], getState(), flags);
		
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag = super.serializeNBT();
		final NBTTagList wrapperListTag = new NBTTagList();
		for(IBlockData other : others){
			final NBTTagCompound wrapperTag = new NBTTagCompound();
			wrapperTag.setString(TAG_WRAPPER_FACTORY_KEY, ForgeCreeperHeal.getProxy().getBlockClassKeyBuilder().convertToString(other.getState().getBlock()));
			
			final NBTTagCompound otherTag = other.serializeNBT();
			wrapperTag.setTag(TAG_WRAPPER_DATA, otherTag);
			wrapperListTag.appendTag(wrapperTag);
		}
		
		tag.setTag(TAG_WRAPPERS, wrapperListTag);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		super.deserializeNBT(tag);
		
		final NBTTagList wrapperListTag = tag.getTagList(TAG_WRAPPERS, NBT.TAG_COMPOUND);
		this.others = new IBlockData[wrapperListTag.tagCount()];
		for(int i = 0; i<wrapperListTag.tagCount(); i++) {
			final NBTTagCompound wrapperTag = wrapperListTag.getCompoundTagAt(i);
			final String factoryKey = wrapperTag.getString(TAG_WRAPPER_FACTORY_KEY);
			final NBTTagCompound healableTag = wrapperTag.getCompoundTag(TAG_WRAPPER_DATA);
			
			IBlockData healable = (IBlockData) ForgeCreeperHeal.getProxy().getBlockDataFactory().getDefault().create(healableTag);
			this.others[i] = healable;
		}
		
	}
	
	public static boolean isMultipleBlockData(final NBTTagCompound tag) {
		return tag.hasKey(TAG_WRAPPERS);
	}
}
