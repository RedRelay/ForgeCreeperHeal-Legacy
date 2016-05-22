package fr.eyzox.forgecreeperheal.healer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.healer.scheduler.BlockScheduler;
import fr.eyzox.forgecreeperheal.healer.scheduler.IScheduler;
import fr.eyzox.forgecreeperheal.healer.tick.ITickProvider;
import fr.eyzox.forgecreeperheal.healer.tick.TickProvider;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.minecraft.util.ChunkDataProvider;
import fr.eyzox.ticktimeline.Node;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class HealerFactory {
	
	private ITickProvider tickProvider = new TickProvider();
	
	public HealerFactory() {}
	
	/**
	 * Create healers
	 * @param world - The world the healer must be created
	 * @param healables - The block to be heal (no specified order)
	 */
	public Map<ChunkCoordIntPair, Collection<Node<? extends ISerializableHealable>>> create(final WorldServer world, final Collection<? extends IBlockData> healables) {
		
		final IScheduler<IBlockData> scheduler = new BlockScheduler(healables);
		final ChunkDataProvider<DispatchedTimeline> dispatchedTimelines = this.scheduleHealables(scheduler);
		
		final Map<ChunkCoordIntPair, Collection<Node<? extends ISerializableHealable>>> result = new HashMap<ChunkCoordIntPair, Collection<Node<? extends ISerializableHealable>>>(dispatchedTimelines.size());
		for(final Entry<ChunkCoordIntPair, DispatchedTimeline> entry : dispatchedTimelines.entrySet()) {
			result.put(entry.getKey(), entry.getValue().timeline);
		}
		
		return result;
	}

	private ChunkDataProvider<DispatchedTimeline> scheduleHealables(final IScheduler<IBlockData> scheduler) {
		
		final ChunkDataProvider<DispatchedTimeline> dispatchedTimelines = new ChunkDataProvider<DispatchedTimeline>();
		
		int globalTickCounter = 0;
		
		while(scheduler.hasNext()) {
			final IBlockData healable = scheduler.next();
			
			//Retrieve chunk for this block
			final int xChunk = healable.getPos().getX() >> 4;
			final int zChunk = healable.getPos().getZ() >> 4;
			
			//Retrieve or create DispatchedTimeline for a chunk
			DispatchedTimeline dispatchedTimeline = dispatchedTimelines.get(ChunkCoordIntPair.chunkXZ2Int(xChunk, zChunk));
			if(dispatchedTimeline == null) {
				dispatchedTimeline = new DispatchedTimeline();
				dispatchedTimelines.put(new ChunkCoordIntPair(xChunk, zChunk), dispatchedTimeline);
			}
			
			//Generate tick
			final int providedTick = tickProvider.getNextTick();
			globalTickCounter += providedTick;
			final int tick = globalTickCounter - dispatchedTimeline.tickCounter;
			dispatchedTimeline.tickCounter += providedTick;
			
			//Create TickContainer
			final Node<ISerializableHealable> container = new Node<ISerializableHealable>();
			container.setTick(tick);
			container.setData(healable);
			
			
			dispatchedTimeline.timeline.add(container);
		}
		
		//Set minimal tick before heal
		final int minTickBeforeHeal = tickProvider.getStartTick();
		for(final DispatchedTimeline timeline : dispatchedTimelines.values()) {
			final Node<? extends ISerializableHealable> firstNode = timeline.timeline.getFirst();
			firstNode.setTick(minTickBeforeHeal + firstNode.getTick());
		}
		
		return dispatchedTimelines;
	}

	private static class DispatchedTimeline {
		LinkedList<Node<? extends ISerializableHealable>> timeline = new LinkedList<Node<? extends ISerializableHealable>>();
		int tickCounter;
	}
	
}