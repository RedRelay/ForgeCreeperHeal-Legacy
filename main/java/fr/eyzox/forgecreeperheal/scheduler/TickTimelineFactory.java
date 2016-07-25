package fr.eyzox.forgecreeperheal.scheduler;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.dependencygraph.interfaces.IData;
import fr.eyzox.forgecreeperheal.healer.IChunked;
import fr.eyzox.forgecreeperheal.healer.IHealable;
import fr.eyzox.forgecreeperheal.scheduler.custom.IScheduler;
import fr.eyzox.forgecreeperheal.scheduler.tick.ITickProvider;
import fr.eyzox.forgecreeperheal.scheduler.tick.TickProvider;
import fr.eyzox.minecraft.util.ChunkDataProvider;
import fr.eyzox.ticktimeline.Node;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class TickTimelineFactory {
	
	private ITickProvider tickProvider = new TickProvider();
	
	public TickTimelineFactory() {}
	
	/**
	 * Create healers
	 * @param world - The world the healer must be created
	 * @param healables - The block to be heal (no specified order)
	 */
	public <T extends IHealable & IChunked & IData<?>> Map<ChunkCoordIntPair, Collection<Node<T>>> create(final WorldServer world, final IScheduler<T> scheduler) {
		
		final ChunkDataProvider<DispatchedTimeline<T>> dispatchedTimelines = this.scheduleHealables(scheduler);
		
		final Map<ChunkCoordIntPair, Collection<Node<T>>> result = new HashMap<ChunkCoordIntPair, Collection<Node<T>>>(dispatchedTimelines.size());
		for(final Entry<ChunkCoordIntPair, DispatchedTimeline<T>> entry : dispatchedTimelines.entrySet()) {
			result.put(entry.getKey(), entry.getValue().timeline);
		}
		
		return result;
	}

	private <T extends IHealable & IChunked & IData<?>> ChunkDataProvider<DispatchedTimeline<T>> scheduleHealables(final IScheduler<T> scheduler) {
		
		final ChunkDataProvider<DispatchedTimeline<T>> dispatchedTimelines = new ChunkDataProvider<DispatchedTimeline<T>>();
		
		int globalTickCounter = 0;
		
		while(scheduler.hasNext()) {
			final T healable = scheduler.next();
			
			final int chunkX = healable.getChunkX();
			final int chunkZ = healable.getChunkZ();
			
			//Retrieve or create DispatchedTimeline for a chunk
			DispatchedTimeline dispatchedTimeline = dispatchedTimelines.get(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
			if(dispatchedTimeline == null) {
				dispatchedTimeline = new DispatchedTimeline();
				dispatchedTimelines.put(new ChunkCoordIntPair(chunkX, chunkZ), dispatchedTimeline);
			}
			
			//Generate tick
			final int providedTick = tickProvider.getNextTick();
			globalTickCounter += providedTick;
			final int tick = globalTickCounter - dispatchedTimeline.tickCounter;
			dispatchedTimeline.tickCounter += providedTick;
			
			//Create TickContainer
			final Node<T> container = new Node<T>();
			container.setTick(tick);
			container.setData(healable);
			
			
			dispatchedTimeline.timeline.add(container);
		}
		
		//Set minimal tick before heal
		final int minTickBeforeHeal = tickProvider.getStartTick();
		for(final DispatchedTimeline<T> timeline : dispatchedTimelines.values()) {
			final Node<T> firstNode = timeline.timeline.getFirst();
			firstNode.setTick(minTickBeforeHeal + firstNode.getTick());
		}
		
		return dispatchedTimelines;
	}

	private static class DispatchedTimeline<T> {
		LinkedList<Node<T>> timeline = new LinkedList<Node<T>>();
		int tickCounter;
		
		@Override
		public String toString() {
			return timeline.toString();
		}
	}
	
}