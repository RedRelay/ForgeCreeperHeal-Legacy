package fr.eyzox.forgecreeperheal.healer;

import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

public class Healer implements INBTSerializable<NBTTagCompound>{
	
	private final static String TAG_TIMELINE = "TIMELINE";
	
	private final Chunk chunk;
	private final SerializableTimeline timeline = new SerializableTimeline();
	
	private boolean loaded;
	
	public Healer(final Chunk chunk) {
		this.chunk = chunk;
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

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(TAG_TIMELINE, this.timeline.serializeNBT());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		this.timeline.deserializeNBT(tag.getCompoundTag(TAG_TIMELINE));
	}
	
}
