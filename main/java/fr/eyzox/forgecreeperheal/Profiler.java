package fr.eyzox.forgecreeperheal;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import fr.eyzox.forgecreeperheal.network.profiler.ProfilerInfoMessage;
import fr.eyzox.forgecreeperheal.worldhealer.BlockData;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;
import fr.eyzox.ticklinkedlist.TickContainer;

public class Profiler {
	
	private List<EntityPlayerMP> listeners = new LinkedList<EntityPlayerMP>();
	private boolean serverWatch;
	
	private WorldHealer worldHealer;
	
	private int totalTicks;
	private int currentTick;
	private long tickStart;
	private long explosionStart;
	private double avgTick;
	private double avgExplosion;
	
	private long blocksUsed;
	
	
	public Profiler(WorldHealer worldHealer) {
		this(worldHealer, 20);
	}
	
	public Profiler(WorldHealer worldHealer, int totalTicks) {
		this.totalTicks = totalTicks;
		this.worldHealer = worldHealer;
	}
	
	public void begin() {
		if(currentTick >= totalTicks) {
			currentTick = 0;
			this.avgTick = 0;
			this.avgExplosion = 0;
		}else {
			currentTick++;
		}
	}
	
	public void tickStart() {
		this.tickStart = System.currentTimeMillis();
	}
	
	public void tickStop() {
		long  tickStop = System.currentTimeMillis();
		if(currentTick == 0) {
			this.avgTick = (tickStop-tickStart);
		}else {
			this.avgTick = (this.avgTick + (tickStop-tickStart))/2;
		}
		
	}
	
	public void explosionStart() {
		this.explosionStart = System.currentTimeMillis();
	}
	
	public void explosionStop() {
		long explosionStop = System.currentTimeMillis();
		if(this.avgExplosion == 0) {
			this.avgExplosion = explosionStop - explosionStart;
		}else {
			this.avgExplosion = (this.avgExplosion + (explosionStop-explosionStart))/2;
		}
		
	}
	
	public void handleMemoryUse(LinkedList<TickContainer<Collection<BlockData>>> data) {
		if(currentTick < totalTicks) return;
		this.blocksUsed = 0;
		
		Iterator<TickContainer<Collection<BlockData>>> it = data.descendingIterator();
		TickContainer<Collection<BlockData>> cursor = null;
		while(it.hasNext()) {
			cursor = it.next();
			this.blocksUsed += cursor.getData().size();
		}
	}
	
	public void addListener(EntityPlayerMP player) {
		listeners.add(player);
	}
	
	public void removeListener(EntityPlayerMP player) {
		listeners.remove(player);
	}
	
	public List<EntityPlayerMP> getListeners() {
		return listeners;
	}
	
	public boolean isServerWatch() {
		return serverWatch;
	}

	public void setServerWatch(boolean serverWatch) {
		this.serverWatch = serverWatch;
	}

	public void report() {
		if(currentTick < totalTicks) return;
		double totalTicks = this.avgTick+this.avgExplosion;
		if(serverWatch) ForgeCreeperHeal.getLogger().info(String.format("[PROFILER:%s#%d] Tick : %.4f ms, Memory usage : %d blocks", this.worldHealer.getWorld().getWorldInfo().getWorldName(), this.worldHealer.getWorld().provider.dimensionId, totalTicks, this.blocksUsed));
		for(EntityPlayerMP player : listeners) {
			if(MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player)) {
				ForgeCreeperHeal.getChannel().sendTo(new ProfilerInfoMessage(worldHealer.getWorld(), totalTicks, blocksUsed), player);
			}else {
				worldHealer.disableProfiler(player);
			}
		}
	}
}
