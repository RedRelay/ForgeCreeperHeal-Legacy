package com.lothrazar.creeperheal.handler;

import com.lothrazar.creeperheal.ConfigRegistry;
import com.lothrazar.creeperheal.ForgeCreeperHeal;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExplosionEventHandler {

  @SubscribeEvent
  public void onDetonate(ExplosionEvent.Detonate event) {
    if (event.getLevel().isClientSide) {
      return;
    }
    Entity exploder = event.getExplosion().getSourceMob();
    boolean isCreeper = exploder instanceof Creeper;
    if (ConfigRegistry.isOnlyCreepers() == false ||
        (ConfigRegistry.isOnlyCreepers() && isCreeper)) {
      //if only creeper is false, dont need to check
      //only creepers allowed in, so it better be one
      WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((ServerLevel) event.getLevel());
      if (worldHealer != null) {
        worldHealer.onDetonate(event);
      }
    }
  }
}
