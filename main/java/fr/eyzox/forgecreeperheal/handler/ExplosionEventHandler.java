package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ExplosionEventHandler {

	@SubscribeEvent
	public void onDetonate(ExplosionEvent.Detonate event) {
		//TODO FromPlayer exception config
		if(!event.getWorld().isRemote) {
			Entity exploder = event.getExplosion().getExplosivePlacedBy();
				//(Entity) Reflect.getDataFromField(this.exploder, event.getExplosion());
			
			//we no longer need reflection since we have the getExplosivePlacedBy  function

			if(exploder != null && !ForgeCreeperHeal.getConfig().getFromEntityException().contains(exploder.getClass())) {
				WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.getWorld());
				if(worldHealer != null) {
					worldHealer.onDetonate(event);
				}
			}
		}
	}
}
