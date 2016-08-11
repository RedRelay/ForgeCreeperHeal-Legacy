package fr.eyzox.minecraft.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public class ChunkDataProvider<E> extends ConcurrentHashMap<ChunkCoordIntPair, E>{
	private static final String NULL_KEY = "Null key is not allowed";
	private static final String CLASS_KEYS = "Key should be a "+ChunkCoordIntPair.class.getCanonicalName();
	
	private LongHashMap map = new LongHashMap();

	@Override
	public void clear() {
		super.clear();
		map = new LongHashMap();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final ChunkDataProvider<E> clone = (ChunkDataProvider<E>) super.clone();
		final LongHashMap map = new LongHashMap();
		for(final Map.Entry<ChunkCoordIntPair, E> entry : this.entrySet()) {
			map.add(this.getMapKey(entry.getKey()), entry.getValue());
		}
		clone.map = map;
		return clone;
	}

	@Override
	public E put(ChunkCoordIntPair key, E value) {
		if(key == null) {
			throw new NullPointerException(NULL_KEY);
		}
		map.add(this.getMapKey(key), value);
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
		
		if(key instanceof ChunkCoordIntPair) {
			return this.remove((ChunkCoordIntPair)key);
		}
		
		if(key instanceof Chunk) {
			return this.remove((Chunk)key);
		}
		
		throw new ClassCastException(CLASS_KEYS);
		
	}
	
	public E remove(ChunkCoordIntPair chunk) {
		if(chunk == null) {
			throw new NullPointerException(NULL_KEY);
		}
		map.remove(this.getMapKey((ChunkCoordIntPair)chunk));
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
		return map.containsItem(key);
	}
	
	public boolean containsKey(final Chunk chunk) {
		return containsKey(this.getMapKey(chunk));
	}
	
	public E get(final long key) {
		return (E) map.getValueByKey(key);
	}
	
	public E get(final Chunk chunk) {
		return this.get(this.getMapKey(chunk));
	}
	
	
	
	private long getMapKey(final ChunkCoordIntPair chunk) {
		return ChunkCoordIntPair.chunkXZ2Int(chunk.chunkXPos, chunk.chunkZPos);
	}
	
	private long getMapKey(final Chunk chunk) {
		return ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition);
	}
}
