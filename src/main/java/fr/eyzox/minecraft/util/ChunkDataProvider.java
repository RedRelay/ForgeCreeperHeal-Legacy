package fr.eyzox.minecraft.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;


public class ChunkDataProvider<E> extends ConcurrentHashMap<ChunkPos, E>{
	private static final String NULL_KEY = "Null key is not allowed";
	private static final String CLASS_KEYS = "Key should be a "+ChunkPos.class.getCanonicalName();
	
	private Long2ObjectOpenHashMap<E> map = new Long2ObjectOpenHashMap<E>();

	@Override
	public void clear() {
		super.clear();
		map.clear();
		map.trim();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final ChunkDataProvider<E> clone = (ChunkDataProvider<E>) super.clone();
		final Long2ObjectOpenHashMap<E> map = new Long2ObjectOpenHashMap<E>();
		for(final Map.Entry<ChunkPos, E> entry : this.entrySet()) {
			map.put(this.getMapKey(entry.getKey()), entry.getValue());
		}
		clone.map = map;
		return clone;
	}

	@Override
	public E put(ChunkPos key, E value) {
		if(key == null) {
			throw new NullPointerException(NULL_KEY);
		}
		map.put(this.getMapKey(key), value);
		return super.put(key, value);
	}
	
	public E put(Chunk chunk, E value) {
		if(chunk == null) {
			throw new NullPointerException(NULL_KEY);
		}
		return this.put(chunk.getChunkCoordIntPair(), value);
	}

	@Override
	public E remove(Object key) {
		if(key == null) {
			throw new NullPointerException(NULL_KEY);
		}
		
		if(key instanceof ChunkPos) {
			return this.remove((ChunkPos)key);
		}
		
		if(key instanceof Chunk) {
			return this.remove((Chunk)key);
		}
		
		throw new ClassCastException(CLASS_KEYS);
		
	}
	
	public E remove(ChunkPos chunk) {
		if(chunk == null) {
			throw new NullPointerException(NULL_KEY);
		}
		map.remove(this.getMapKey((ChunkPos)chunk));
		return super.remove(chunk);
	}
	
	public E remove(Chunk chunk) {
		if(chunk == null) {
			throw new NullPointerException(NULL_KEY);
		}
		
		if(map.remove(this.getMapKey(chunk)) != null) {
			return super.remove(chunk.getChunkCoordIntPair());
		}else {
			return null;
		}
	}
	
	public boolean containsKey(final long key) {
		return map.containsKey(key);
	}
	
	public boolean containsKey(final Chunk chunk) {
		return containsKey(this.getMapKey(chunk));
	}
	
	public E get(final long key) {
		return map.get(key);
	}
	
	public E get(final Chunk chunk) {
		return this.get(this.getMapKey(chunk));
	}
	
	public Long2ObjectMap<E> getLong2ObjectMap() {
		return map;
	}
	
	private long getMapKey(final ChunkPos chunk) {
		return ChunkPos.chunkXZ2Int(chunk.chunkXPos, chunk.chunkZPos);
	}
	
	private long getMapKey(final Chunk chunk) {
		return ChunkPos.chunkXZ2Int(chunk.xPosition, chunk.zPosition);
	}
}
