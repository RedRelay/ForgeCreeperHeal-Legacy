package fr.eyzox.forgecreeperheal.serial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.ticktimeline.Node;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

public class TimelineSerializer {

	private static final String TAG_WRAPPERS = "wrappers";
	private static final String TAG_NODES = "nodes";
	
	private static final String TAG_NODE_CONTENTS = "contents";
	private static final String TAG_NODE_TICK = "tick";
	
	private static final String TAG_WRAPPER = "wrapper";
	private static final String TAG_WRAPPER_DATA = "data";
	
	private static final TimelineSerializer INSTANCE = new TimelineSerializer();
	
	public static TimelineSerializer getInstance() { return INSTANCE;}
	
	private TimelineSerializer() {
	}
	
	public <T extends ISerialWrapperProvider> NBTTagCompound serializeNBT(final TickTimeline<T> timeline) {
		final NBTTagCompound tag = new NBTTagCompound();
		
		final NBTTagList nodeListTag = new NBTTagList();
		
		final BiMap<Integer, String> wrapperBag = createWrapperBag(timeline);
		
		for(final Node<Collection<T>> node : timeline.getTimeline()){
			final NBTTagCompound nodeTag = this.serializeNode(wrapperBag, node);
			nodeListTag.appendTag(nodeTag);
		}
		
		tag.setTag(TAG_WRAPPERS, serializeWrapperBag(wrapperBag));
		tag.setTag(TAG_NODES, nodeListTag);
		
		return tag;
		
	}
	
	public <T extends ISerialWrapperProvider> TickTimeline<T> deserializeNBT(final NBTTagCompound tag) {
		final TickTimeline<T> timeline = new TickTimeline<T>();
		
		final NBTTagList wrapperBagTag = tag.getTagList(TAG_WRAPPERS, NBT.TAG_STRING);
		final ArrayList<ISerialWrapper<T>> wrapperBag = unserializeWrapperBag(wrapperBagTag);
		
		final NBTTagList nodeListTag = tag.getTagList(TAG_NODES, NBT.TAG_COMPOUND);
		for(int i=0; i<nodeListTag.tagCount(); i++) {
			final NBTTagCompound nodeTag = nodeListTag.getCompoundTagAt(i);
			final Node<Collection<T>> node = unserializeNode(wrapperBag, nodeTag);
			timeline.getTimeline().add(node);
		}
		return timeline;
	}
	
	private <T extends ISerialWrapperProvider> NBTTagCompound serializeNode(final BiMap<Integer, String> wrapperBag, final Node<Collection<T>> node) {
		final NBTTagCompound nodeTag = new NBTTagCompound();
		nodeTag.setInteger(TAG_NODE_TICK, node.getTick());
		final NBTTagList contentListTag = new NBTTagList();
		for(final T nodeData : node.getData()) {
			
			final ISerialWrapper<T> wrapper = (ISerialWrapper<T>) nodeData.getSerialWrapper();
			final Integer wrapperId = wrapperBag.inverse().get(wrapper.getClass().getName());
			
			final NBTTagCompound contentTag = new NBTTagCompound();
			contentTag.setInteger(TAG_WRAPPER, wrapperId.intValue());
			contentTag.setTag(TAG_WRAPPER_DATA, wrapper.serialize(nodeData));
			
			contentListTag.appendTag(contentTag);
		}
		nodeTag.setTag(TAG_NODE_CONTENTS, contentListTag);
		return nodeTag;
	}
	
	private <T extends ISerialWrapperProvider> Node<Collection<T>> unserializeNode(final ArrayList<ISerialWrapper<T>> wrapperBag, final NBTTagCompound tag) {
		final Node<Collection<T>> node = new Node<Collection<T>>();
		node.setTick(tag.getInteger(TAG_NODE_TICK));
		node.setData(new LinkedList<T>());
		
		final NBTTagList contentListTag = tag.getTagList(TAG_NODE_CONTENTS, NBT.TAG_COMPOUND);
		for(int i = 0; i<contentListTag.tagCount(); i++) {
			final NBTTagCompound contentTag = contentListTag.getCompoundTagAt(i);
			final int wrapperId = contentTag.getInteger(TAG_WRAPPER);
			final NBTTagCompound wrapperData = contentTag.getCompoundTag(TAG_WRAPPER_DATA);
			
			if(wrapperId < 0 || wrapperId >= wrapperBag.size() || wrapperBag.get(wrapperId) == null) {
				ForgeCreeperHeal.getLogger().error("Invalid wrapper id : "+wrapperId);
			}else {
				node.getData().add(wrapperBag.get(wrapperId).unserialize(wrapperData));
			}
		}
		
		return node;
	}

	private NBTTagList serializeWrapperBag(BiMap<Integer, String> wrapperBag) {
		final String[] wrappers = new String[wrapperBag.size()];
		for(Entry<Integer, String> entry : wrapperBag.entrySet()) {
			wrappers[entry.getKey()] = entry.getValue();
		}
		
		final NBTTagList list = new NBTTagList();
		for(int i = 0; i<wrappers.length; i++) {
			list.appendTag(new NBTTagString(wrappers[i]));
		}
		
		return list;
	}
	
	private <T extends ISerialWrapperProvider> ArrayList<ISerialWrapper<T>> unserializeWrapperBag(final NBTTagList tag) {
		final ArrayList<ISerialWrapper<T>> wrappers = new ArrayList<ISerialWrapper<T>>(tag.tagCount());
		for(int i = 0; i<tag.tagCount(); i++) {
			String className = tag.getStringTagAt(i);
			
			Class<? extends ISerialWrapper<T>> wrapperClass = null;
			ISerialWrapper<T> wrapper = null;
			
			try {
				wrapperClass = (Class<? extends ISerialWrapper<T>>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				ForgeCreeperHeal.getLogger().error("Unable to find wrapper class : "+className);
			} catch (ClassCastException e2) {
				ForgeCreeperHeal.getLogger().error(String.format("Error while unserializing wrapper class \"%s\" : %s", className, e2.getMessage()));
			}
			
			if(wrapperClass != null) {
				try {
					wrapper = wrapperClass.newInstance();
				} catch (ReflectiveOperationException e) {
					ForgeCreeperHeal.getLogger().error(String.format("Unable to instanciate wrapper \"%s\" : %s", className, e.getMessage()));
				}
			}
			
			wrappers.add(wrapper);
		}
		
		return wrappers;
	}
	
	private <T extends ISerialWrapperProvider> BiMap<Integer, String> createWrapperBag(final TickTimeline<T> timeline) {
		final BiMap<Integer, String> wrapperBag = HashBiMap.create();
		for(Node<Collection<T>> node : timeline.getTimeline()) {
			for(T data : node.getData()) {
				final String className = data.getSerialWrapper().getClass().getName();
				if(!wrapperBag.containsValue(className)) {
					wrapperBag.put(Integer.valueOf(wrapperBag.size()), className);
				}
			}
		}
		return wrapperBag;
	}
	
	
}
