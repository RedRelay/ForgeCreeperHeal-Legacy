package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

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
