package fr.eyzox.forgecreeperheal.healer;

import java.util.HashSet;
import java.util.Set;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;

public class WorldHealerData extends WorldSavedData implements INBTSerializable<NBTTagCompound> {

	public static final String KEY = ForgeCreeperHeal.MODID+":WHD";
	
	private final static String CHUNKS_WITH_HEALER_TAG = "chunks";
	
	private final Set<ChunkPos> chunksWithHealer = new HashSet<ChunkPos>();
	
	public WorldHealerData(String name) {
		super(name);
	}

	public void handleChunk(final ChunkPos chunk) {
		if(this.chunksWithHealer.add(chunk)) {
			this.markDirty();
		}
	}
	
	public void unhandleChunk(final ChunkPos chunk) {
		if(this.chunksWithHealer.remove(chunk)) {
			this.markDirty();
		}
	}
	
	public Set<ChunkPos> getChunksWithHealer() {
		return chunksWithHealer;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList chunksTagList = nbt.getTagList(CHUNKS_WITH_HEALER_TAG, NBT.TAG_INT_ARRAY);
		for(int i = 0 ; i < chunksTagList.tagCount(); i++) {
			final int[] array = chunksTagList.getIntArrayAt(i);
			if(array.length >= 2) {
				chunksWithHealer.add(new ChunkPos(array[0], array[1]));
			}else {
				//TODO serial exception
			}
		}
		
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList chunksTagList = new NBTTagList();
		for(final ChunkPos chunk : chunksWithHealer) {
			chunksTagList.appendTag(new NBTTagIntArray(new int[]{chunk.chunkXPos, chunk.chunkZPos}));
		}
		nbt.setTag(CHUNKS_WITH_HEALER_TAG, chunksTagList);
		return nbt;

	}
	
	public static WorldHealerData load(WorldServer w) {
		MapStorage storage = w.getPerWorldStorage();
		WorldHealerData result = (WorldHealerData)storage.getOrLoadData(WorldHealerData.class, KEY);
		if(result == null) {
			result = new WorldHealerData(KEY);
			storage.setData(KEY, result);
		}
		return result;
	}

}
