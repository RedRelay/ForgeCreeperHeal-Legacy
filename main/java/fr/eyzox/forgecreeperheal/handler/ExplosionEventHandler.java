package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ExplosionEventHandler {

	@SubscribeEvent
	public void onDetonate(ExplosionEvent.Detonate event) {
		if(!event.world.isRemote && event.explosion.exploder != null && !ForgeCreeperHeal.getConfig().getFromEntityException().contains(event.explosion.exploder.getClass())) {
			WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.world);
			if(worldHealer != null) {
				worldHealer.onDetonate(event);
			}
		}
	}

}
