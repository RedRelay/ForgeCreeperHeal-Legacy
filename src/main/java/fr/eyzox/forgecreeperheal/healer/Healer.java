package fr.eyzox.forgecreeperheal.healer;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;
import fr.eyzox.forgecreeperheal.serial.TimelineSerializer;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

public class Healer implements INBTSerializable<NBTTagCompound>{
	
	private final static String TAG_TIMELINE = "TIMELINE";
	
	private final Chunk chunk;
	private boolean loaded;
	
	private TickTimeline<BlockData> timeline;
	
	public Healer(final Chunk chunk) {
		this.chunk = chunk;
		this.timeline = new TickTimeline<BlockData>();
	}
	
	public Healer(final Chunk chunk, final NBTTagCompound tag) {
		this.chunk = chunk;
		this.deserializeNBT(tag);
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public TickTimeline<BlockData> getTimeline() {
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
		
		if(timeline == null) {
			throw new ForgeCreeperHealException("Unable to serialize : TickTimeline cannot be null");
		}
		
		final TimelineSerializer timelineSerializer = TimelineSerializer.getInstance();
		
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(TAG_TIMELINE, timelineSerializer.serializeNBT(timeline));
		
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		final TimelineSerializer timelineSerializer = TimelineSerializer.getInstance();
		this.timeline = timelineSerializer.deserializeNBT(tag.getCompoundTag(TAG_TIMELINE));
	}
	
}
