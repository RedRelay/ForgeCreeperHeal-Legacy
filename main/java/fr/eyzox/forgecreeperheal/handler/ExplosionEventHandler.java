package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ExplosionEventHandler {

	@SubscribeEvent
	public void onDetonate(ExplosionEvent.Detonate event) {
		if(event.getWorld().isRemote) {
			return;
		}
		
		Entity exploder = event.getExplosion().getExplosivePlacedBy();
		
		boolean isCreeper = (exploder != null && exploder instanceof EntityCreeper);

		if(ForgeCreeperHeal.getConfig().isOnlyCreepers() == false || //anything is allowed in, NOT only creepers
				(ForgeCreeperHeal.getConfig().isOnlyCreepers() && isCreeper)){//only creepers allowed in, so it better be one
			
			WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.getWorld());
			if(worldHealer != null) {
				worldHealer.onDetonate(event);
			}
		}
	}
}
