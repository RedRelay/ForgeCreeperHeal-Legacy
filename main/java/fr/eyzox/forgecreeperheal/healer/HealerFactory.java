package fr.eyzox.forgecreeperheal.healer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.builder.ISerializableHealableBuilder;
import fr.eyzox.forgecreeperheal.factory.Factory;
import fr.eyzox.forgecreeperheal.healer.scheduler.BlockScheduler;
import fr.eyzox.forgecreeperheal.healer.scheduler.IScheduler;
import fr.eyzox.forgecreeperheal.healer.tick.ITickProvider;
import fr.eyzox.forgecreeperheal.healer.tick.TickProvider;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.forgecreeperheal.serial.wrapper.ISerialWrapper;
import fr.eyzox.ticktimeline.Node;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;

public class HealerFactory {
	
	private static final String TAG_NODES = "nodes";
	
	private static final String TAG_NODE_WRAPPERS = "wrappers";
	private static final String TAG_NODE_TICK = "tick";
	
	private static final String TAG_WRAPPER_CLASS = "class";
	private static final String TAG_WRAPPER_FACTORY_KEY = "key";
	private static final String TAG_WRAPPER_DATA = "data";
	
	private ITickProvider tickProvider = new TickProvider();
	
	public HealerFactory() {}
	
	/**
	 * Create healers
	 * @param world - The world the healer must be created
	 * @param healables - The block to be heal (no specified order)
	 */
	public void create(final WorldServer world, final Collection<? extends IBlockData> healables) {
		
		final IScheduler<IBlockData> scheduler = new BlockScheduler(healables);
		
		final Map<ChunkCoordIntPair, DispatchedTimeline> dispatchedTimelines = this.scheduleHealables(scheduler);
		
		final HealerManager manager = ForgeCreeperHeal.getHealerManager();
		for(final Entry<ChunkCoordIntPair, DispatchedTimeline> entry : dispatchedTimelines.entrySet()) {
			//Retrieve or create timeline for this chunk
			TickTimeline<ISerializableHealable> timeline = manager.getHealer(world, entry.getKey());
			if(timeline == null) {
				timeline = new TickTimeline<ISerializableHealable>();
				manager.putHealer(world, entry.getKey(), timeline);
			}
			timeline.add(entry.getValue().timeline);
		}
	}

	private Map<ChunkCoordIntPair, DispatchedTimeline> scheduleHealables(final IScheduler<IBlockData> scheduler) {
		
		final Map<ChunkCoordIntPair, DispatchedTimeline> dispatchedTimelines = new HashMap<ChunkCoordIntPair, DispatchedTimeline>();
		
		int globalTickCounter = 0;
		
		while(scheduler.hasNext()) {
			final IBlockData healable = scheduler.next();
			
			//Retrieve chunk for this block
			final ChunkCoordIntPair chunk = new ChunkCoordIntPair(healable.getPos().getX() >> 4, healable.getPos().getZ() >> 4);
			
			//Retrieve or create DispatchedTimeline for a chunk
			DispatchedTimeline dispatchedTimeline = dispatchedTimelines.get(chunk);
			if(dispatchedTimeline == null) {
				dispatchedTimeline = new DispatchedTimeline();
				dispatchedTimelines.put(chunk, dispatchedTimeline);
			}
			
			//Generate tick
			final int providedTick = tickProvider.provideTick();
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
		final int minTickBeforeHeal = tickProvider.getMinimumTickBeforeHeal();
		for(final DispatchedTimeline timeline : dispatchedTimelines.values()) {
			final Node<? extends ISerializableHealable> firstNode = timeline.timeline.getFirst();
			firstNode.setTick(minTickBeforeHeal + firstNode.getTick());
		}
		
		return dispatchedTimelines;
	}
	
	public NBTTagCompound serialize(final TickTimeline<ISerializableHealable> timeline) {
		final NBTTagCompound tag = new NBTTagCompound();
		
		final NBTTagList nodeListTag = new NBTTagList();
		for(final Node<Collection<ISerializableHealable>> node : timeline.getTimeline()){
			final NBTTagCompound nodeTag = this.serializeNode(node);
			nodeListTag.appendTag(nodeTag);
		}
		
		tag.setTag(TAG_NODES, nodeListTag);
		
		return tag;
		
	}
	
	private NBTTagCompound serializeNode(final Node<Collection<ISerializableHealable>> node) {
		final NBTTagCompound nodeTag = new NBTTagCompound();
		nodeTag.setInteger(TAG_NODE_TICK, node.getTick());
		final NBTTagList wrapperListTag = new NBTTagList();
		for(final ISerializableHealable healable : node.getData()) {
			
			final ISerialWrapper<?> wrapper = healable.getSerialWrapper();
			
			final NBTTagCompound wrapperTag = new NBTTagCompound();
			wrapperTag.setString(TAG_WRAPPER_CLASS, wrapper.getClass().getCanonicalName());
			wrapperTag.setString(TAG_WRAPPER_FACTORY_KEY, wrapper.getFactoryKey());
			
			final NBTTagCompound healableTag = new NBTTagCompound();
			healable.writeToNBT(healableTag);
			
			wrapperTag.setTag(TAG_WRAPPER_DATA, healableTag);
			
			wrapperListTag.appendTag(wrapperTag);
		}
		nodeTag.setTag(TAG_NODE_WRAPPERS, wrapperListTag);
		return nodeTag;
	}
	
	public TickTimeline<ISerializableHealable> unserialize(final NBTTagCompound tag) {
		
		final List<Node<Collection<ISerializableHealable>>> nodes = new LinkedList<Node<Collection<ISerializableHealable>>>();
		final NBTTagList nodeListTag = tag.getTagList(TAG_NODES, NBT.TAG_COMPOUND);
		for(int i=0; i<nodeListTag.tagCount(); i++) {
			final NBTTagCompound nodeTag = nodeListTag.getCompoundTagAt(i);
			final Node<Collection<ISerializableHealable>> node = unserializeNode(nodeTag);
			nodes.add(node);
		}
		
		final TickTimeline<ISerializableHealable> timeline = new TickTimeline<ISerializableHealable>(nodes);
		return timeline;
	}
	
	private Node<Collection<ISerializableHealable>> unserializeNode(final NBTTagCompound tag) {
		final Node<Collection<ISerializableHealable>> node = new Node<Collection<ISerializableHealable>>();
		node.setTick(tag.getInteger(TAG_NODE_TICK));
		node.setData(new LinkedList<ISerializableHealable>());
		
		final NBTTagList wrapperListTag = tag.getTagList(TAG_NODE_WRAPPERS, NBT.TAG_COMPOUND);
		for(int i = 0; i<wrapperListTag.tagCount(); i++) {
			final NBTTagCompound wrapperTag = wrapperListTag.getCompoundTagAt(i);
			
			final String wrapperClassStr = wrapperTag.getString(TAG_WRAPPER_CLASS);
			Class<? extends ISerialWrapper<?>> wrapperClass = null;
			try {
				wrapperClass = (Class<? extends ISerialWrapper<?>>) Class.forName(wrapperClassStr);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				//TODO serial exception
			}
			
			if(wrapperClass != null) {
				ISerialWrapper<?> wrapper = null;
				try {
					wrapper = wrapperClass.newInstance();
				} catch (InstantiationException e) {
					// TODO Serial exception
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// serial exception
					e.printStackTrace();
				}
				
				if(wrapper != null) {
					Factory<?,? extends ISerializableHealableBuilder<?>> factory = wrapper.getFactory();
					ISerializableHealableBuilder<?> healableBuilder = factory.getData(wrapperTag.getString(TAG_WRAPPER_FACTORY_KEY));
					//TODO serial exception on create() ...
					ISerializableHealable healable = healableBuilder.create(wrapperTag.getCompoundTag(TAG_WRAPPER_DATA));
					node.getData().add(healable);
				}
			}
		}
		
		return node;
	}

	private static class DispatchedTimeline {
		LinkedList<Node<? extends ISerializableHealable>> timeline = new LinkedList<Node<? extends ISerializableHealable>>();
		int tickCounter;
	}
	
}