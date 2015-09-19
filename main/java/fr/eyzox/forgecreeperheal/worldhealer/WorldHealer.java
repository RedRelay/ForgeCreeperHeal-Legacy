package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.ExplosionEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.Profiler;
import fr.eyzox.forgecreeperheal.healtimeline.HealTimeline;
import fr.eyzox.forgecreeperheal.healtimeline.factory.HealableFactories;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.timeline.ITimelineElement;

public class WorldHealer extends WorldSavedData{
	
	private World world;
	private List<HealTimeline> healTimelines = new LinkedList<HealTimeline>();

	private Profiler profiler;


	public WorldHealer() {
		this(getDataStorageKey());
	}

	public WorldHealer(String key) {
		super(key);
	}

	public void onTick() {
		if(profiler != null) {
			profiler.begin();
			profiler.tickStart();
		}
		
		Iterator<HealTimeline> it = healTimelines.iterator();
		while(it.hasNext()) {
			HealTimeline healTimeline = it.next();
			healTimeline.onTick();
			if(!healTimeline.hasAvailable()) {
				it.remove();
			}
		}
		

		if(profiler != null) {
			profiler.tickStop();
			//profiler.handleMemoryUse(healTask.getLinkedList());
			profiler.report();
		}
	}

	public void onDetonate(ExplosionEvent.Detonate event) {

		if(profiler != null) {
			profiler.explosionStart();
		}
		
		if(event.getAffectedBlocks().isEmpty()) return;

		Collection<IHealable> healables = getHealables(event.world, event.getAffectedBlocks());
		
		healTimelines.add(new HealTimeline(this.world,healables));
		
		for(IHealable data : healables) {
			data.removeFromWorld(event.world);
		}
		
		if(profiler != null) {
			profiler.explosionStop();
		}

	}

	private Collection<IHealable> getHealables(World world, Collection<BlockPos> affectedBlocks) {
		Set<IHealable> healables = new HashSet<IHealable>();
		for(BlockPos pos : affectedBlocks) {
			IBlockState blockstate = world.getBlockState(pos);
			
			IHealable data = HealableFactories.getInstance().create(world, pos, blockstate);
			
			if(data != null) {
				healables.add(data);
			}
			
		}
		return healables;
	}

	public void healAll() {
		
		for(HealTimeline healTimeline : healTimelines) {
			for(ITimelineElement data : healTimeline) {
				((IHealable)data).heal(world, 2);
			}
		}
		
		healTimelines.clear();
	}


	public World getWorld() {
		return world;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		/*
		NBTTagList tagList = (NBTTagList) tag.getTag("healtasklist");
		for(int i=0; i<tagList.tagCount(); i++) {
			NBTTagCompound tickContainerTag = tagList.getCompoundTagAt(i);
			int ticksLeft = tickContainerTag.getInteger("ticks");
			LinkedList<BlockData> blockDataList = new LinkedList<BlockData>();
			NBTTagList blockDataListTag = (NBTTagList) tickContainerTag.getTag("blockdatalist");
			for(int j=0;j<blockDataListTag.tagCount(); j++) {
				NBTTagCompound blockDataTag = blockDataListTag.getCompoundTagAt(j);
				BlockData blockData = new BlockData();
				blockData.readFromNBT(blockDataTag);
				blockDataList.add(blockData);
			}
			healTask.getLinkedList().addLast(new TickContainer<Collection<BlockData>>(ticksLeft, blockDataList));
		}
		*/

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		/*
		NBTTagList tagList = new NBTTagList();
		for(TickContainer<Collection<BlockData>> tc : this.healTask.getLinkedList()) {
			NBTTagCompound tickContainerTag = new NBTTagCompound();
			tickContainerTag.setInteger("ticks", tc.getTick());
			NBTTagList blockDataListTag = new NBTTagList();
			for(BlockData blockData : tc.getData()) {
				NBTTagCompound blockDataTag = new NBTTagCompound();
				blockData.writeToNBT(blockDataTag);
				blockDataListTag.appendTag(blockDataTag);
			}
			tickContainerTag.setTag("blockdatalist", blockDataListTag);
			tagList.appendTag(tickContainerTag);
		}
		tag.setTag("healtasklist", tagList);
		*/
	}

	public static WorldHealer loadWorldHealer(World w) {
		MapStorage storage = w.getPerWorldStorage();
		final String KEY = getDataStorageKey();
		WorldHealer result = (WorldHealer)storage.loadData(WorldHealer.class, KEY);
		if (result == null) {
			result = new WorldHealer(KEY);
			storage.setData(KEY, result);
			ForgeCreeperHeal.getLogger().info("Unable to find data for world "+w.getWorldInfo().getWorldName()+"["+w.provider.getDimensionId()+"], new data created");
		}

		result.world = w;

		return result;
	}

	public static String getDataStorageKey() {
		return ForgeCreeperHeal.MODID+":"+WorldHealer.class.getSimpleName();
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	public void enableProfiler(ICommandSender sender) {
		if(profiler == null) this.profiler = new Profiler(this);
		profiler.addListener(sender);
	}

	public void disableProfiler(ICommandSender sender) {
		if(profiler != null){
			profiler.removeListener(sender);
		}
	}

	public void disableProfiler() {
		this.profiler = null;
	}

	public boolean isProfilerEnabled() {
		return this.profiler != null;
	}

	public Profiler getProfiler() {
		return profiler;
	}

}
