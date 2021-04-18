package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ExplosionEventHandler {

  @SubscribeEvent
  public void onDetonate(ExplosionEvent.Detonate event) {
    if (event.getWorld().isRemote) {
      return;
    }
    Entity exploder = event.getExplosion().getExplosivePlacedBy();
    //TODO: config for other explosion sources
    if (exploder instanceof CreeperEntity) { //only creepers allowed in, so it better be one
      WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((ServerWorld) event.getWorld());
      if (worldHealer != null) {
        worldHealer.onDetonate(event);
      }
    }
  }
}
