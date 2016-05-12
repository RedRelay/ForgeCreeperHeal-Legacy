package fr.eyzox.forgecreeperheal.healer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class HealerManager {
	
	private final WorldServer world;
	private final Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> healers = new ConcurrentHashMap<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>();
	
	public HealerManager(final WorldServer world) {
		this.world = world;
	}
	
	public void load(final ChunkCoordIntPair chunk, final TickTimeline<ISerializableHealable> timeline) {
		healers.put(chunk, timeline);
	}
	
	public void unload(final ChunkCoordIntPair chunk) {
		healers.remove(chunk);
	}
	
	public void put(final ChunkCoordIntPair chunk, final TickTimeline<ISerializableHealable> timeline) {
		if(!world.theChunkProviderServer.chunkExists(chunk.chunkXPos, chunk.chunkZPos)) {
			world.theChunkProviderServer.loadChunk(chunk.chunkXPos, chunk.chunkZPos);
		}
		this.load(chunk, timeline);
		//TODO notify put for world saved data
	}
	
	public void remove(final ChunkCoordIntPair chunk) {
		this.unload(chunk);
		//TODO notify remove for world saved data
	}
	
	public TickTimeline<ISerializableHealable> get(final ChunkCoordIntPair chunk) {
		TickTimeline<ISerializableHealable> timeline = healers.get(chunk);
		if(timeline == null) {
			//Maybe timeline is not loaded yet, so we load the chunk
			world.theChunkProviderServer.loadChunk(chunk.chunkXPos, chunk.chunkZPos);
			timeline = healers.get(chunk);
			//If timeline = null, it means there are no timeline currently for this chunk 
		}
		return timeline;
	}
	
	public WorldServer getWorld() {
		return world;
	}
	
	public Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> getHealers() {
		return healers;
	}

}
