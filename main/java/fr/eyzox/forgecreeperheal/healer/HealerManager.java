package fr.eyzox.forgecreeperheal.healer;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.Node;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class HealerManager {
	
	private final WorldServer world;
	private final Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> healers = new ConcurrentHashMap<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>();
	private final WorldHealerData worldHealerData;
	
	public HealerManager(final WorldServer world) {
		this.world = world;
		this.worldHealerData = WorldHealerData.load(world);
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
		this.worldHealerData.handleChunk(chunk);
	}
	
	public void remove(final ChunkCoordIntPair chunk) {
		this.unload(chunk);
		this.worldHealerData.unhandleChunk(chunk);
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
	
	public void heal() {
		healLoaded();
		for(final ChunkCoordIntPair chunk : this.worldHealerData.getChunksWithHealer()) {
			world.theChunkProviderServer.loadChunk(chunk.chunkXPos, chunk.chunkZPos);
		}
		healLoaded();
	}
	
	private void healLoaded() {
		for(final Entry<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> entry : healers.entrySet()) {
			final TickTimeline<ISerializableHealable> timeline = entry.getValue();
			for(final Node<Collection<ISerializableHealable>> node : timeline.getTimeline()) {
				for(final ISerializableHealable healable : node.getData()) {
					healable.heal(world, 7);
				}
			}
			this.worldHealerData.unhandleChunk(entry.getKey());
		}
		this.healers.clear();
	}
	
	public WorldServer getWorld() {
		return world;
	}
	
	public Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> getHealers() {
		return healers;
	}

}
