package fr.eyzox.forgecreeperheal.serial;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealerSerialException;
import fr.eyzox.ticktimeline.Node;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class TimelineSerializer {

	private static final String TAG_NODES = "nodes";
	
	private static final String TAG_NODE_CONTENTS = "contents";
	private static final String TAG_NODE_TICK = "tick";
	
	private static final TimelineSerializer INSTANCE = new TimelineSerializer();
	
	public static TimelineSerializer getInstance() { return INSTANCE;}
	
	private TimelineSerializer() {
	}
	
	public <T extends ISerialWrapperProvider<T>> NBTTagCompound serializeNBT(final TickTimeline<T> timeline) {
		final NBTTagCompound tag = new NBTTagCompound();
		
		final NBTTagList nodeListTag = new NBTTagList();
		
		for(final Node<Collection<T>> node : timeline.getTimeline()){
			final NBTTagCompound nodeTag = this.serializeNode(node);
			nodeListTag.appendTag(nodeTag);
		}
		tag.setTag(TAG_NODES, nodeListTag);
		
		return tag;
		
	}
	
	public <T extends ISerialWrapperProvider<T>> TickTimeline<T> deserializeNBT(final NBTTagCompound tag) {
		final TickTimeline<T> timeline = new TickTimeline<T>();
		
		final NBTTagList nodeListTag = tag.getTagList(TAG_NODES, NBT.TAG_COMPOUND);
		for(int i=0; i<nodeListTag.tagCount(); i++) {
			final NBTTagCompound nodeTag = nodeListTag.getCompoundTagAt(i);
			final Node<Collection<T>> node = unserializeNode(nodeTag);
			timeline.getTimeline().add(node);
		}
		return timeline;
	}
	
	private <T extends ISerialWrapperProvider<T>> NBTTagCompound serializeNode(final Node<Collection<T>> node) {
		final NBTTagCompound nodeTag = new NBTTagCompound();
		nodeTag.setInteger(TAG_NODE_TICK, node.getTick());
		
		final NBTTagList contentListTag = new NBTTagList();
		for(final T nodeData : node.getData()) {
			contentListTag.appendTag(SerialUtils.serializeWrappedData(nodeData.getSerialWrapper(), nodeData));
		}
		nodeTag.setTag(TAG_NODE_CONTENTS, contentListTag);
		return nodeTag;
	}
	
	private <T extends ISerialWrapperProvider<T>> Node<Collection<T>> unserializeNode(final NBTTagCompound tag) {
		final Node<Collection<T>> node = new Node<Collection<T>>();
		node.setTick(tag.getInteger(TAG_NODE_TICK));
		
		final List<T> dataList = new LinkedList<T>();
		final NBTTagList contentListTag = tag.getTagList(TAG_NODE_CONTENTS, NBT.TAG_COMPOUND);
		for(int i = 0; i<contentListTag.tagCount(); i++) {
			try {
				final T data = SerialUtils.unserializeWrappedData(contentListTag.getCompoundTagAt(i));
				dataList.add(data);
			} catch (ForgeCreeperHealerSerialException e) {
				ForgeCreeperHeal.getLogger().error("Error while unserialize data : "+e.getMessage());
			}
		}
		
		node.setData(dataList);
		
		return node;
	}
	
}
