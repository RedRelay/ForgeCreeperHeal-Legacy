package com.lothrazar.creeperheal.handler;

import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldEventHandler {

  private Map<ServerLevel, WorldHealer> worldHealers = new HashMap<ServerLevel, WorldHealer>();

  public Map<ServerLevel, WorldHealer> getWorldHealers() {
    return worldHealers;
  }

  @SubscribeEvent
  public void onLoad(WorldEvent.Load event) {
    if (!event.getWorld().isClientSide()
        && event.getWorld() instanceof ServerLevel) {
      worldHealers.put((ServerLevel) event.getWorld(), WorldHealer.loadWorldHealer((ServerLevel) event.getWorld()));
    }
  }

  @SubscribeEvent
  public void onUnload(WorldEvent.Unload event) {
    if (!event.getWorld().isClientSide()) {
      worldHealers.remove(event.getWorld());
    }
  }
}
