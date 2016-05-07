package fr.eyzox.forgecreeperheal.healer;

import java.util.Map;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class HealerManager {
	
	public HealerManager() {}
	
	public TickTimeline<ISerializableHealable> getHealer(final WorldServer world, final ChunkCoordIntPair chunk) {
		TickTimeline<ISerializableHealable> timeline = ForgeCreeperHeal.getProxy().getChunkEventHandler().getHealer(world, chunk);
		if(timeline == null) {
			//Maybe timeline is not loaded yet, so we load the chunk
			world.theChunkProviderServer.loadChunk(chunk.chunkXPos, chunk.chunkZPos);
			timeline = ForgeCreeperHeal.getProxy().getChunkEventHandler().getHealer(world, chunk);
			//If timeline = null, it means there are no timeline currently for this chunk 
		}
		return timeline;
	}
	
	public void putHealer(final WorldServer world, final ChunkCoordIntPair chunk, final TickTimeline<ISerializableHealable> healer) {
		if(!world.theChunkProviderServer.chunkExists(chunk.chunkXPos, chunk.chunkZPos)) {
			world.theChunkProviderServer.loadChunk(chunk.chunkXPos, chunk.chunkZPos);
		}
		ForgeCreeperHeal.getProxy().getChunkEventHandler().putHealer(world, chunk, healer);
	}
	
	public Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> getLoadedHealers(final WorldServer world) {
		final Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> loadedHealersForWorld = ForgeCreeperHeal.getProxy().getChunkEventHandler().getHealers(world);
		return loadedHealersForWorld;
	}
	
	public Map<WorldServer, Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>> getLoadedHealers() {
		return ForgeCreeperHeal.getProxy().getChunkEventHandler().getLoadedHealers();
	}
	
	public void removeHealer(final WorldServer world, final ChunkCoordIntPair chunk) {
		ForgeCreeperHeal.getProxy().getChunkEventHandler().removeHealer(world, chunk);
	}

}
