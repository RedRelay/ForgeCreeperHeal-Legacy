package fr.eyzox.forgecreeperheal.serial;

import java.util.Collection;
import java.util.LinkedList;

import fr.eyzox.forgecreeperheal.builder.ISerializableHealableBuilder;
import fr.eyzox.forgecreeperheal.factory.Factory;
import fr.eyzox.forgecreeperheal.serial.wrapper.ISerialWrapper;
import fr.eyzox.ticktimeline.Node;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Constants.NBT;

public class TimelineSerializer {

	private static final String TAG_NODES = "nodes";
	
	private static final String TAG_NODE_WRAPPERS = "wrappers";
	private static final String TAG_NODE_TICK = "tick";
	
	private static final String TAG_WRAPPER_CLASS = "class";
	private static final String TAG_WRAPPER_FACTORY_KEY = "key";
	private static final String TAG_WRAPPER_DATA = "data";
	
	private TickTimeline<ISerializableHealable> timeline;
	
	public NBTTagCompound serializeNBT(final TickTimeline<ISerializableHealable> timeline) {
		final NBTTagCompound tag = new NBTTagCompound();
		
		final NBTTagList nodeListTag = new NBTTagList();
		for(final Node<Collection<ISerializableHealable>> node : timeline.getTimeline()){
			final NBTTagCompound nodeTag = this.serializeNode(node);
			nodeListTag.appendTag(nodeTag);
		}
		
		tag.setTag(TAG_NODES, nodeListTag);
		
		return tag;
		
	}
	
	public TickTimeline<ISerializableHealable> deserializeNBT(final NBTTagCompound tag) {
		final TickTimeline<ISerializableHealable> timeline = new TickTimeline<ISerializableHealable>();
		final NBTTagList nodeListTag = tag.getTagList(TAG_NODES, NBT.TAG_COMPOUND);
		for(int i=0; i<nodeListTag.tagCount(); i++) {
			final NBTTagCompound nodeTag = nodeListTag.getCompoundTagAt(i);
			final Node<Collection<ISerializableHealable>> node = unserializeNode(nodeTag);
			timeline.getTimeline().add(node);
		}
		return timeline;
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
			
			final NBTTagCompound healableTag = healable.serializeNBT();
			
			wrapperTag.setTag(TAG_WRAPPER_DATA, healableTag);
			
			wrapperListTag.appendTag(wrapperTag);
		}
		nodeTag.setTag(TAG_NODE_WRAPPERS, wrapperListTag);
		return nodeTag;
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

}
