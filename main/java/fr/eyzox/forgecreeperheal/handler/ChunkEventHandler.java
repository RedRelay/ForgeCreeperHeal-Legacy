package fr.eyzox.forgecreeperheal.handler;

import java.util.HashSet;
import java.util.Set;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunkEventHandler implements IEventHandler{

	private static final String FCH_TAG = "FCHTAG";
	private static final String HEALER_TAG = "HEALER";
	
	private final Set<ChunkPos> unloadQueue = new HashSet<ChunkPos>();
	
	@SubscribeEvent
	public void onChunkDataLoad(final ChunkDataEvent.Load event) {
		if(event.getWorld().isRemote) return;
		
		final NBTTagCompound FCHTag = event.getData().getCompoundTag(FCH_TAG);
		if(!FCHTag.hasNoTags()) {
			final NBTTagCompound healerTag = FCHTag.getCompoundTag(HEALER_TAG);
			if(!healerTag.hasNoTags()) {
				
				//TODO Error while unserial
				final TickTimeline<ISerializableHealable> healer = ForgeCreeperHeal.getHealerFactory().unserialize(healerTag);
				
				final WorldServer world = (WorldServer) event.getWorld();
				final ChunkPos chunk = event.getChunk().getChunkCoordIntPair();
				
				ForgeCreeperHeal.getHealerManager(world).load(chunk, healer);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onChunkDataSave(final ChunkDataEvent.Save event) {
		if(event.getWorld().isRemote) return;

		final WorldServer world = (WorldServer) event.getWorld();
		final ChunkPos chunk = event.getChunk().getChunkCoordIntPair();

		final TickTimeline<ISerializableHealable> healer = ForgeCreeperHeal.getHealerManager(world).getHealers().get(chunk);
		if(healer != null) {
			//TODO Error while serial
			final NBTTagCompound healerTag = ForgeCreeperHeal.getHealerFactory().serialize(healer);

			final NBTTagCompound FCHTag = event.getData().getCompoundTag(FCH_TAG);
			FCHTag.setTag(HEALER_TAG, healerTag);
			event.getData().setTag(FCH_TAG, FCHTag);
			
			//If chunk is unloaded, unhandle its healer
			if(unloadQueue.remove(chunk)) {
				ForgeCreeperHeal.getHealerManager(((WorldServer) event.getWorld())).unload(event.getChunk().getChunkCoordIntPair());
			}
		}
	}
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event) {
		if(event.getWorld().isRemote) return;
		
		final ChunkPos chunk = event.getChunk().getChunkCoordIntPair();
		
		if(ForgeCreeperHeal.getHealerManager((WorldServer)event.getWorld()).get(chunk) != null) {
			unloadQueue.add(chunk);
		}
		
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

}
