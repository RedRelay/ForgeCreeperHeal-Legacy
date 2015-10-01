package fr.eyzox.forgecreeperheal.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunEventHandler {

	private Map<Chunk, Queue<IHealable>> unloadedChunkWaitingTask = new HashMap<Chunk, Queue<IHealable>>();
	
	@SubscribeEvent
	public void onChunkUnloading(ChunkEvent.Unload event) {
		if(event.world.isRemote || unloadedChunkWaitingTask.isEmpty()) return;
		
		Queue<IHealable> queue = unloadedChunkWaitingTask.get(event.getChunk());
		
		if(queue == null) return;
		
		for(IHealable healable : queue) {
			healable.heal(event.world, 2);
		}
		
	}
	
}
