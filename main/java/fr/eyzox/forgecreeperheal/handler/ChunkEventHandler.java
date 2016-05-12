package fr.eyzox.forgecreeperheal.handler;

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
				
				ForgeCreeperHeal.getHealerManager(world).load(chunk, healer);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onChunkDataSave(final ChunkDataEvent.Save event) {
		if(event.world.isRemote) return;

		final WorldServer world = (WorldServer) event.world;
		final ChunkCoordIntPair chunk = event.getChunk().getChunkCoordIntPair();

		final TickTimeline<ISerializableHealable> healer = ForgeCreeperHeal.getHealerManager(world).getHealers().get(chunk);
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
		ForgeCreeperHeal.getHealerManager(((WorldServer) event.world)).unload(event.getChunk().getChunkCoordIntPair());
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

}
