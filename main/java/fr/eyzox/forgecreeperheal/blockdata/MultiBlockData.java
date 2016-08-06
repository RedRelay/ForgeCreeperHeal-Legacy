package fr.eyzox.forgecreeperheal.blockdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.eyzox.dependencygraph.DataKeyProvider;
import fr.eyzox.dependencygraph.MultipleDataKeyProvider;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealerSerialException;
import fr.eyzox.forgecreeperheal.healer.WorldRemover;
import fr.eyzox.forgecreeperheal.serial.SerialUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class MultiBlockData extends BlockData {

	private static final String TAG_OTHERS = "others";
	
	private Collection<BlockData> others;
	
	private Set<BlockPos> allBlockPos;
	
	public MultiBlockData(final BlockPos pos, final IBlockState state, final Collection<? extends BlockData> others) {
		super(pos, state);
		
		this.allBlockPos = new HashSet<BlockPos>(1+others.size());
		this.allBlockPos.add(this.getPos());
		for(final BlockData other : others) {
			
			if(other.isMultiple()) {
				throw new ForgeCreeperHealException("MultiBlockData is not recursive");
			}
			
			final boolean added = allBlockPos.add(other.getPos());
			if(!added) {
				throw new ForgeCreeperHealException("Duplicate BlockPos in MultiBlockData");
			}
		}
		
		this.others = new ArrayList<BlockData>(others);
	}
	
	public MultiBlockData(final NBTTagCompound tag) {
		super(tag);
	}
	
	@Override
	public DataKeyProvider<BlockPos> getDataKeyProvider() {
		return new MultipleDataKeyProvider<BlockPos>(allBlockPos);
	}

	private Map<BlockPos, IBlockState> buildOldStateMap(final World world) {
		final Map<BlockPos, IBlockState> oldStateMap = new HashMap<BlockPos, IBlockState>(allBlockPos.size());
		for(final BlockPos pos : allBlockPos) {
			oldStateMap.put(pos, world.getBlockState(pos));
		}
		return oldStateMap;
	}
	
	@Override
	public void heal(World world, int flags) {
		//Building old blockstate index
		final Map<BlockPos, IBlockState> oldStateMap = buildOldStateMap(world);
		
		//Silent Heal
		super.heal(world, 0);
		for(BlockData other : others) {
			other.heal(world, 0);
		}
		
		//Update
		updateWorld(world, this, oldStateMap.get(this.getPos()), flags);
		for(final BlockData other : others) {
			updateWorld(world, other, oldStateMap.get(other.getPos()), flags);
		}

	}
	
	private void updateWorld(final World w, final BlockData data, final IBlockState oldState, int flags) {
		w.markAndNotifyBlock(data.getPos(), w.getChunkFromBlockCoords(data.getPos()), oldState, data.getState(), flags);
	}

	@Override
	public void remove(final WorldRemover remover) {
		super.remove(remover);
		for(final BlockData other : others) {
			other.remove(remover);
		}
	}
	
	@Override
	public boolean isMultiple() {
		return true;
	}
	
	public static boolean isMultiple(final NBTTagCompound tag) {
		return tag.hasKey(TAG_OTHERS, NBT.TAG_LIST);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag = super.serializeNBT();
		
		final NBTTagList wrapperListTag = new NBTTagList();
		for(BlockData other : others){
			wrapperListTag.appendTag(SerialUtils.serializeWrappedData(other.getSerialWrapper(), other));
		}
		
		tag.setTag(TAG_OTHERS, wrapperListTag);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		super.deserializeNBT(tag);
		
		final NBTTagList wrapperListTag = tag.getTagList(TAG_OTHERS, NBT.TAG_COMPOUND);
		this.others = new ArrayList<BlockData>(wrapperListTag.tagCount());
		
		for(int i = 0; i<wrapperListTag.tagCount(); i++) {
			
			BlockData data = null;
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
}
