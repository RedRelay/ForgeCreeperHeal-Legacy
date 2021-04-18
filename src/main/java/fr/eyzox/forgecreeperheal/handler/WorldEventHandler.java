package fr.eyzox.forgecreeperheal.handler;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class WorldEventHandler {

  private Map<ServerWorld, WorldHealer> worldHealers = new HashMap<ServerWorld, WorldHealer>();

  public Map<ServerWorld, WorldHealer> getWorldHealers() {
    return worldHealers;
  }

  @SubscribeEvent
  public void onLoad(WorldEvent.Load event) {
    if (!event.getWorld().isRemote()
        && event.getWorld() instanceof ServerWorld) {
      worldHealers.put((ServerWorld) event.getWorld(), WorldHealer.loadWorldHealer((ServerWorld) event.getWorld()));
    }
  }

  @SubscribeEvent
  public void onUnload(WorldEvent.Unload event) {
    if (!event.getWorld().isRemote()) {
      worldHealers.remove(event.getWorld());
    }
  }
}
