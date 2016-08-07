package fr.eyzox.forgecreeperheal.handler;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldEventHandler implements IEventHandler{

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if(event.world.isRemote) return;

		final WorldServer world = (WorldServer) event.world;
		ForgeCreeperHeal.getProxy().getHealerManagers().put(world, new HealerManager(world));

	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if(event.world.isRemote) return;
		ForgeCreeperHeal.getProxy().getHealerManagers().remove((WorldServer) event.world);
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if(event.world.isRemote || event.phase != TickEvent.Phase.START) return;
		ForgeCreeperHeal.getHealerManager((WorldServer) event.world).tick();
	}

	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
