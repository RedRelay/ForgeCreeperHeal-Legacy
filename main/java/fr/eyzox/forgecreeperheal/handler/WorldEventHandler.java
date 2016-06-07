package fr.eyzox.forgecreeperheal.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class WorldEventHandler {

	private Map<WorldServer, WorldHealer> worldHealers = new HashMap<WorldServer, WorldHealer>();
	
	public Map<WorldServer, WorldHealer> getWorldHealers() {
		return worldHealers;
	}
	
	@SubscribeEvent
	public void onLoad(WorldEvent.Load event) {
		if(!event.getWorld().isRemote) {
			worldHealers.put((WorldServer) event.getWorld(), WorldHealer.loadWorldHealer(event.getWorld()));
		}
	}
	
	@SubscribeEvent
	public void onUnload(WorldEvent.Unload event) {
		if(!event.getWorld().isRemote) {
			worldHealers.remove(event.getWorld());
		}
	}
}
