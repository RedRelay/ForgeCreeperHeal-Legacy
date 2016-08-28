package fr.eyzox.forgecreeperheal.healer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.minecraft.util.ChunkDataProvider;
import fr.eyzox.ticktimeline.Node;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class HealerManager {
	
	private final WorldServer world;
	private final ChunkDataProvider<Healer> loadedHealers = new ChunkDataProvider<Healer>();
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
		final ChunkCoordIntPair chunkKey = chunk.getChunkCoordIntPair();
		
		healer.setLoaded(true);
		
		this.loadedHealers.put(chunkKey, healer);
		chunk.setChunkModified();
		this.worldHealerData.handleChunk(chunkKey);
	}
	
	/**
	 * Loads and removes the healer for a chunk
	 * @param chunkKey
	 */
	public void unhook(final ChunkCoordIntPair chunkKey) {
		final Chunk chunk = world.getChunkFromChunkCoords(chunkKey.chunkXPos, chunkKey.chunkZPos);
		this.loadedHealers.remove(chunkKey);
		chunk.setChunkModified();
		this.worldHealerData.unhandleChunk(chunkKey);
	}
	
	/**
	 * Loads and gets the healer for a chunk 
	 * @param chunk
	 * @return
	 */
	public Healer load(final ChunkCoordIntPair chunk) {
		return loadedHealers.get(world.getChunkFromChunkCoords(chunk.chunkXPos, chunk.chunkZPos));
	}
	
	/**
	 * Heals all healer from the world
	 */
	public void heal() {
		healLoaded();
		for(final ChunkCoordIntPair chunk : this.worldHealerData.getChunksWithHealer()) {
			world.theChunkProviderServer.loadChunk(chunk.chunkXPos, chunk.chunkZPos);
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
			for(final Entry<ChunkCoordIntPair, Healer> entry : this.loadedHealers.entrySet()) {
				
				final Healer healer = entry.getValue();
				
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
			for(final Entry<ChunkCoordIntPair, Healer> entry : loadedHealers.entrySet()) {
				for(final Node<Collection<BlockData>> node : entry.getValue().getTimeline().getTimeline()) {
					for(BlockData data : node.getData()) {
						data.heal(worldHealer);
					}
				}
				this.worldHealerData.unhandleChunk(entry.getKey());
			}
			worldHealer.update(3);
		}
		this.loadedHealers.clear();
	}
	
	public WorldServer getWorld() {
		return world;
	}
	
	public ChunkDataProvider<Healer> getLoadedHealers() {
		return loadedHealers;
	}

}
