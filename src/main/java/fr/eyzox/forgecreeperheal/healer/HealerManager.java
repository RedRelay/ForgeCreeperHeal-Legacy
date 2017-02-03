package fr.eyzox.forgecreeperheal.healer;

import java.util.Collection;
import java.util.LinkedList;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.ticktimeline.Node;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class HealerManager {
	
	private final WorldServer world;
	private final Long2ObjectOpenHashMap<Healer> loadedHealers = new Long2ObjectOpenHashMap<Healer>();
	private final WorldHealerData worldHealerData;
	private final WorldHealer worldHealer;
	
	public HealerManager(final WorldServer world) {
		this.world = world;
		this.worldHealerData = WorldHealerData.load(world);
		this.worldHealer = new WorldHealer(world);
	}
	
	/**
	 * Loads and puts a new healer for a chunk
	 * @param chunkKey
	 * @param timeline
	 */
	public void hook(final Healer healer) {
		final Chunk chunk = world.getChunkFromChunkCoords(healer.getChunk().xPosition, healer.getChunk().zPosition);
		final ChunkPos chunkKey = chunk.getChunkCoordIntPair();
		
		healer.setLoaded(true);
		
		this.loadedHealers.put(ChunkPos.chunkXZ2Int(chunkKey.chunkXPos, chunkKey.chunkZPos), healer);
		chunk.setChunkModified();
		this.worldHealerData.handleChunk(chunkKey);
	}
	
	/**
	 * Loads and removes the healer for a chunk
	 * @param chunkKey
	 */
	public void unhook(final ChunkPos chunkKey) {
		final Chunk chunk = world.getChunkFromChunkCoords(chunkKey.chunkXPos, chunkKey.chunkZPos);
		this.loadedHealers.remove(ChunkPos.chunkXZ2Int(chunk.xPosition, chunk.zPosition));
		chunk.setChunkModified();
		this.worldHealerData.unhandleChunk(chunkKey);
	}
	
	/**
	 * Loads and gets the healer for a chunk 
	 * @param chunk
	 * @return
	 */
	public Healer load(final ChunkPos chunkKey) {
		final Chunk chunk = world.getChunkFromChunkCoords(chunkKey.chunkXPos, chunkKey.chunkZPos);
		return loadedHealers.get(ChunkPos.chunkXZ2Int(chunk.xPosition, chunk.zPosition));
	}
	
	/**
	 * Heals all healer from the world
	 */
	public void heal() {
		healLoaded();
		for(final ChunkPos chunk : this.worldHealerData.getChunksWithHealer()) {
			world.getChunkProvider().loadChunk(chunk.chunkXPos, chunk.chunkZPos);
		}
		healLoaded();
	}
	
	/**
	 * Called by {@link WorldEventHandler#onWorldTick(net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent)}
	 */
	public void tick() {
		if(this.loadedHealers.isEmpty()) {
			return;
		}
		
		final LinkedList<Healer> emptyHealers = new LinkedList<Healer>();
		synchronized(worldHealer) {
			for(final Healer healer : loadedHealers.values()) {
				
				final Collection<BlockData> healables = healer.getTimeline().tick();
				if(healables != null) {
					for(BlockData data : healables) {
						data.heal(worldHealer);
					}
	
					if(healer.getTimeline().isEmpty()) {
						emptyHealers.add(healer);
					}
				}
				
				healer.getChunk().setChunkModified();
				
			}
			worldHealer.update(3);
		}
		
		if(!emptyHealers.isEmpty()) {
			for(final Healer emptyHealer : emptyHealers) {
				this.unhook(emptyHealer.getChunk().getChunkCoordIntPair());
			}
		}
	}
	
	/**
	 * Heals only loaded healers
	 */
	private void healLoaded() {
		synchronized (worldHealer) {
			for(final Healer healer : loadedHealers.values()) {
				for(final Node<Collection<BlockData>> node : healer.getTimeline().getTimeline()) {
					for(BlockData data : node.getData()) {
						data.heal(worldHealer);
					}
				}
				this.worldHealerData.unhandleChunk(healer.getChunk().getChunkCoordIntPair());
			}
			worldHealer.update(3);
		}
		this.loadedHealers.clear();
		this.loadedHealers.trim();
	}
	
	public WorldServer getWorld() {
		return world;
	}
	
	public Long2ObjectOpenHashMap<Healer> getLoadedHealers() {
		return loadedHealers;
	}

}
