package com.lothrazar.creeperheal.handler;

import com.lothrazar.creeperheal.ConfigRegistry;
import com.lothrazar.creeperheal.ForgeCreeperHeal;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExplosionEventHandler {

  @SubscribeEvent
  public void onDetonate(ExplosionEvent.Detonate event) {
    if (event.getWorld().isRemote) {
      return;
    }
    Entity exploder = event.getExplosion().getExplosivePlacedBy();
    boolean isCreeper = exploder instanceof CreeperEntity;
    //TODO: config for other explosion sources
    if (ConfigRegistry.isOnlyCreepers() == false ||
        (ConfigRegistry.isOnlyCreepers() && isCreeper)) {
      //if only creeper is false, dont need to check
      //only creepers allowed in, so it better be one
      WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((ServerWorld) event.getWorld());
      if (worldHealer != null) {
        worldHealer.onDetonate(event);
      }
    }
  }
}
