package fr.eyzox.forgecreeperheal.healer;

import java.util.HashSet;
import java.util.Set;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants.NBT;

public class WorldHealerData extends WorldSavedData {

	public static final String KEY = ForgeCreeperHeal.MODID+":WHD";
	
	private final static String CHUNKS_WITH_HEALER_TAG = "chunks";
	
	private final Set<ChunkCoordIntPair> chunksWithHealer = new HashSet<ChunkCoordIntPair>();
	
	public WorldHealerData(String name) {
		super(name);
	}

	public void handleChunk(final ChunkCoordIntPair chunk) {
		if(this.chunksWithHealer.add(chunk)) {
			this.markDirty();
		}
	}
	
	public void unhandleChunk(final ChunkCoordIntPair chunk) {
		if(this.chunksWithHealer.remove(chunk)) {
			this.markDirty();
		}
	}
	
	public Set<ChunkCoordIntPair> getChunksWithHealer() {
		return chunksWithHealer;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList chunksTagList = nbt.getTagList(CHUNKS_WITH_HEALER_TAG, NBT.TAG_INT_ARRAY);
		for(int i = 0 ; i < chunksTagList.tagCount(); i++) {
			final int[] array = chunksTagList.getIntArray(i);
			if(array.length >= 2) {
				chunksWithHealer.add(new ChunkCoordIntPair(array[0], array[1]));
			}else {
				ForgeCreeperHeal.getLogger().error("Ignoring a chunk with healer (array length < 2)");
			}
		}
		
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList chunksTagList = new NBTTagList();
		for(final ChunkCoordIntPair chunk : chunksWithHealer) {
			chunksTagList.appendTag(new NBTTagIntArray(new int[]{chunk.chunkXPos, chunk.chunkZPos}));
		}
		nbt.setTag(CHUNKS_WITH_HEALER_TAG, chunksTagList);

	}
	
	public static WorldHealerData load(WorldServer w) {
		MapStorage storage = w.getPerWorldStorage();
		WorldHealerData result = (WorldHealerData)storage.loadData(WorldHealerData.class, KEY);
		if(result == null) {
			result = new WorldHealerData(KEY);
			storage.setData(KEY, result);
		}
		return result;
	}

}
