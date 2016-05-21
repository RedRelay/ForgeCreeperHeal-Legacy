package fr.eyzox.forgecreeperheal.healer;

import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.world.chunk.Chunk;

public class Healer {
	
	private final Chunk chunk;
	private final TickTimeline<ISerializableHealable> timeline;
	
	private boolean loaded;
	
	public Healer(final Chunk chunk, final TickTimeline<ISerializableHealable> timeline) {
		this.chunk = chunk;
		this.timeline = timeline;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public TickTimeline<ISerializableHealable> getTimeline() {
		return timeline;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("{[").append(chunk.xPosition).append(", ").append(chunk.zPosition).append("], ").append(timeline.toString()).append(']').toString();
	}
	
}
