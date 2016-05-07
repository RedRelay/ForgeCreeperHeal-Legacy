package fr.eyzox.forgecreeperheal.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunkEventHandler implements IEventHandler{

	private static final String FCH_TAG = "FCHTAG";
	private static final String HEALER_TAG = "HEALER";
	
	private final Map<WorldServer, Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>> loadedTimelines = new ConcurrentHashMap<WorldServer, Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>>();
	
	
	@SubscribeEvent
	public void onChunkDataLoad(final ChunkDataEvent.Load event) {
		if(event.world.isRemote) return;
		
		final NBTTagCompound FCHTag = event.getData().getCompoundTag(FCH_TAG);
		if(!FCHTag.hasNoTags()) {
			final NBTTagCompound healerTag = FCHTag.getCompoundTag(HEALER_TAG);
			if(!healerTag.hasNoTags()) {
				
				//TODO Error while unserial
				final TickTimeline<ISerializableHealable> healer = ForgeCreeperHeal.getHealerFactory().unserialize(healerTag);
				
				final WorldServer world = (WorldServer) event.world;
				final ChunkCoordIntPair chunk = event.getChunk().getChunkCoordIntPair();
				
				this.putHealer(world, chunk, healer);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onChunkDataSave(final ChunkDataEvent.Save event) {
		if(event.world.isRemote) return;

		final WorldServer world = (WorldServer) event.world;
		final ChunkCoordIntPair chunk = event.getChunk().getChunkCoordIntPair();

		final TickTimeline<ISerializableHealable> healer = this.getHealer(world, chunk);
		if(healer != null) {
			//TODO Error while serial
			final NBTTagCompound healerTag = ForgeCreeperHeal.getHealerFactory().serialize(healer);

			final NBTTagCompound FCHTag = event.getData().getCompoundTag(FCH_TAG);
			FCHTag.setTag(HEALER_TAG, healerTag);
			event.getData().setTag(FCH_TAG, FCHTag);
		}
	}
	
	
	@SubscribeEvent
	public void onChunkUnload(final ChunkEvent.Unload event) {
		if(event.world.isRemote) return;
		
		final WorldServer world = (WorldServer) event.world;
		
		final Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> tmpTimelines = getHealers(world);
		if(tmpTimelines != null) {
			final ChunkCoordIntPair chunk = event.getChunk().getChunkCoordIntPair();
			tmpTimelines.remove(chunk);
			
			if(tmpTimelines.isEmpty()) {
				loadedTimelines.remove(world);
			}
		}
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public Map<WorldServer, Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>> getLoadedHealers() {
		return loadedTimelines;
	}
	
	public TickTimeline<ISerializableHealable> getHealer(final WorldServer world, final ChunkCoordIntPair chunk) {
		final Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> tmpTimelines = getHealers(world);
		return tmpTimelines == null ? null : tmpTimelines.get(chunk);
	}
	
	public Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> getHealers(final WorldServer world) {
		return loadedTimelines.get(world);
	}
	
	public void putHealer(final WorldServer world, final ChunkCoordIntPair chunk, final TickTimeline<ISerializableHealable> healer) {
		Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> tmpTimelines = getHealers(world);
		if(tmpTimelines == null) {
			tmpTimelines = new ConcurrentHashMap<ChunkCoordIntPair, TickTimeline<ISerializableHealable>>();
			loadedTimelines.put(world, tmpTimelines);
		}
		tmpTimelines.put(chunk, healer);
	}
	
	public void removeHealer(final WorldServer world, final ChunkCoordIntPair chunk) {
		Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> tmpTimelines = getHealers(world);
		if(tmpTimelines != null) {
			tmpTimelines.remove(chunk);
			if(tmpTimelines.isEmpty()) {
				loadedTimelines.remove(world);
			}
		}
	}

}
