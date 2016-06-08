package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ExplosionEventHandler {

	@SubscribeEvent
	public void onDetonate(ExplosionEvent.Detonate event) {

		if(!event.getWorld().isRemote) {
			Entity exploder = event.getExplosion().getExplosivePlacedBy();
			//we no longer need reflection since we have the getExplosivePlacedBy  function
				//(Entity) Reflect.getDataFromField(this.exploder, event.getExplosion());
			

			if(exploder != null && exploder instanceof EntityCreeper){
				WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.getWorld());
				if(worldHealer != null) {
					worldHealer.onDetonate(event);
				}
			}
		}
	}
}
