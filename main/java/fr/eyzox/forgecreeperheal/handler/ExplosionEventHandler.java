package fr.eyzox.forgecreeperheal.handler;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ExplosionEventHandler {

//	private Field exploder;

	public ExplosionEventHandler() {
//		exploder = Reflect.getFieldForClass(Explosion.class, "exploder", "field_77283_e");
	}

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

//	private Entity getExploderFromExplosion(Explosion explosion) {
//		Entity entity = null;
//		try {
//			entity = (Entity)exploder.get(explosion);
//		} catch (ReflectiveOperationException e) {
//			CrashReport crash = new CrashReport(e.getLocalizedMessage(), e);
//			FMLCommonHandler.instance().enhanceCrashReport(crash, crash.makeCategory(ForgeCreeperHeal.MODNAME));
//		}
//		return entity;
//	}

}
