package fr.eyzox.forgecreeperheal;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import fr.eyzox.forgecreeperheal.worldhealer.BlockData;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;
import fr.eyzox.ticklinkedlist.TickContainer;

public class Profiler {
	
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
	
	public void report() {
		if(currentTick < totalTicks) return;
		ForgeCreeperHeal.getLogger().info(String.format("[PROFILER:%s#%d] Tick : %.4f ms, Memory usage : %d blocks", this.worldHealer.getWorld().getWorldInfo().getWorldName(), this.worldHealer.getWorld().provider.dimensionId, this.avgTick+this.avgExplosion, this.blocksUsed));
	}
}
