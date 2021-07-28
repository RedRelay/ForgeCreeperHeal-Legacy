package com.lothrazar.creeperheal.handler;

import com.lothrazar.creeperheal.ForgeCreeperHeal;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldTickEventHandler {

  @SubscribeEvent
  public void onWorldTick(TickEvent.WorldTickEvent event) {
    if (!event.world.isClientSide) {
      WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((ServerLevel) event.world);
      if (worldHealer != null) {
        worldHealer.onTick();
      }
    }
  }
}
