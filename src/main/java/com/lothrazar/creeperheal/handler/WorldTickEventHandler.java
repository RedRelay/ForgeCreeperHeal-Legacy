package com.lothrazar.creeperheal.handler;

import com.lothrazar.creeperheal.ForgeCreeperHeal;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldTickEventHandler {

  @SubscribeEvent
  public void onWorldTick(TickEvent.LevelTickEvent event) {
    if (!event.level.isClientSide) {
      WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((ServerLevel) event.level);
      if (worldHealer != null) {
        worldHealer.onTick();
      }
    }
  }
}
