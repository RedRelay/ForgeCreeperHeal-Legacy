package fr.eyzox.forgecreeperheal.blockdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.dependencygraph.DataKeyProvider;
import fr.eyzox.dependencygraph.MultipleDataKeyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealerSerialException;
import fr.eyzox.forgecreeperheal.serial.SerialUtils;
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

	private static final String TAG_OTHERS = "others";
	
	private ArrayList<IBlockData> others;
	
	public MultiBlockData(BlockPos pos, IBlockState state, NBTTagCompound tileEntityTag, final ArrayList<IBlockData> others) {
		super(pos, state, tileEntityTag);
		this.others = others;
	}
	
	public MultiBlockData(final NBTTagCompound tag) {
		super(tag);
	}

	private Collection<BlockPos> buildAllPos() {
		final List<BlockPos> c = new LinkedList<BlockPos>();
		c.add(getPos());
		for(final IBlockData other : others) {
			c.add(other.getPos());
		}
		return c;
	}
	
	@Override
	public DataKeyProvider<BlockPos> getDataKeyProvider() {
		return new MultipleDataKeyProvider<BlockPos>(buildAllPos());
	}
	
	@Override
	public BlockPos[] getAllPos() {
		final BlockPos[] allPos = new BlockPos[1+others.size()];
		allPos[0] = getPos();
		int i = 1;
		for(IBlockData data : others) {
			allPos[i] = data.getPos();
			i++;
		}
		return allPos;
	}
	
	@Override
	public void heal(World world, int flags) {
		
		IBlockState[] oldStates = new IBlockState[1+others.size()];
		
		for(int i = 0; i<others.size(); i++) {
			oldStates[i] = world.getBlockState(others.get(i).getPos());
			others.get(i).heal(world, 0);
		}
		
		oldStates[oldStates.length-1] = world.getBlockState(getPos());
		super.heal(world, 0);
		
		for(int i = 0; i<others.size(); i++) {
			world.markAndNotifyBlock(others.get(i).getPos(), world.getChunkFromBlockCoords(others.get(i).getPos()), oldStates[i], others.get(i).getState(), flags);
		}
		world.markAndNotifyBlock(getPos(), world.getChunkFromBlockCoords(getPos()), oldStates[oldStates.length-1], getState(), flags);
		
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag = super.serializeNBT();
		
		final NBTTagList wrapperListTag = new NBTTagList();
		for(IBlockData other : others){
			wrapperListTag.appendTag(SerialUtils.serializeWrappedData(other, other));
		}
		
		tag.setTag(TAG_OTHERS, wrapperListTag);
		return tag;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		super.deserializeNBT(tag);
		
		final NBTTagList wrapperListTag = tag.getTagList(TAG_OTHERS, NBT.TAG_COMPOUND);
		this.others = new ArrayList<IBlockData>(wrapperListTag.tagCount());
		
		for(int i = 0; i<wrapperListTag.tagCount(); i++) {
			
			IBlockData data = null;
			try {
				data = SerialUtils.unserializeWrappedData(wrapperListTag.getCompoundTagAt(i));
			} catch (ForgeCreeperHealerSerialException e) {
				ForgeCreeperHeal.getLogger().error("Error while unserialize MultiBlockData: "+e.getMessage());
			}
			
			if(data != null) {
				others.add(data);
			}
			
		}
		
	}
	
	public static boolean isMultipleBlockData(final NBTTagCompound tag) {
		return tag.hasKey(TAG_OTHERS);
	}
}
