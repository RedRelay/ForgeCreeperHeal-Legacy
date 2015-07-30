package fr.eyzox.forgecreeperheal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import fr.eyzox.forgecreeperheal.network.ProfilerInfoMessage;
import fr.eyzox.forgecreeperheal.worldhealer.BlockData;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;
import fr.eyzox.ticklinkedlist.TickContainer;

public class Profiler {
	
	private Set<EntityPlayerMP> clientSideModListeners = new HashSet<EntityPlayerMP>();
	private Set<EntityPlayerMP> noClientSideModListeners = new HashSet<EntityPlayerMP>();
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
	
	public void addListener(ICommandSender sender) {
		if(sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			PlayerModData playerModData = PlayerModData.get(player);
			if(playerModData != null && playerModData.MOD_VERSION.compareToIgnoreCase("1.1.0") >= 0) {
				clientSideModListeners.add(player);
			}else {
				noClientSideModListeners.add(player);
			}
		}else {
			serverWatch = true;
		}
		
	}
	
	public void removeListener(ICommandSender sender) {
		if(sender instanceof EntityPlayerMP) {
			clientSideModListeners.remove(sender);
			noClientSideModListeners.remove(sender);
		}else {
			serverWatch = false;
		}
		
		if(getNbListeners() <= 0) {
			worldHealer.disableProfiler();
		}
	}
	
	public int getNbListeners() {
		return (serverWatch?1:0)+clientSideModListeners.size()+noClientSideModListeners.size();
	}

	public void report() {
		if(currentTick < totalTicks) return;
		double totalTicks = this.avgTick+this.avgExplosion;
		if(serverWatch) ForgeCreeperHeal.getLogger().info(String.format("[PROFILER:%s#%d] Tick : %.4f ms, Memory usage : %d blocks", this.worldHealer.getWorld().getWorldInfo().getWorldName(), this.worldHealer.getWorld().provider.getDimensionId(), totalTicks, this.blocksUsed));
		for(EntityPlayerMP player : clientSideModListeners) {
			if(MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player)) {
				ForgeCreeperHeal.getChannel().sendTo(new ProfilerInfoMessage(worldHealer.getWorld(), totalTicks, blocksUsed), player);
			}else {
				worldHealer.disableProfiler(player);
			}
		}
		
		for(EntityPlayerMP player : noClientSideModListeners) {
			if(MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player)) {
				ForgeCreeperHealCommands.addChatMessage(player, new ChatComponentText(String.format("[%s#%d] Tick : %.4f ms, Memory usage : %d blocks", this.worldHealer.getWorld().getWorldInfo().getWorldName(), this.worldHealer.getWorld().provider.getDimensionId(), totalTicks, this.blocksUsed)));
			}else {
				worldHealer.disableProfiler(player);
			}
		}
	}
}
