package com.lothrazar.creeperheal.handler;

import com.lothrazar.creeperheal.ForgeCreeperHeal;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldTickEventHandler {

  @SubscribeEvent
  public void onWorldTick(TickEvent.WorldTickEvent event) {
    if (!event.world.isRemote) {
      WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((ServerWorld) event.world);
      if (worldHealer != null) {
        worldHealer.onTick();
      }
    }
  }
}
