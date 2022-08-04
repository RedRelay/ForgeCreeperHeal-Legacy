package com.lothrazar.creeperheal.handler;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldEventHandler {

  private Map<ServerLevel, WorldHealer> worldHealers = new HashMap<ServerLevel, WorldHealer>();

  public Map<ServerLevel, WorldHealer> getWorldHealers() {
    return worldHealers;
  }

  @SubscribeEvent
  public void onLoad(LevelEvent.Load event) {
    if (!event.getLevel().isClientSide()
        && event.getLevel() instanceof ServerLevel) {
      worldHealers.put((ServerLevel) event.getLevel(), WorldHealer.loadWorldHealer((ServerLevel) event.getLevel()));
    }
  }

  @SubscribeEvent
  public void onUnload(LevelEvent.Unload event) {
    if (!event.getLevel().isClientSide()) {
      worldHealers.remove(event.getLevel());
    }
  }
}
