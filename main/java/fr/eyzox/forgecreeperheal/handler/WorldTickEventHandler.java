package fr.eyzox.forgecreeperheal.handler;

import java.util.Collection;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.BlockData;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class WorldTickEventHandler {
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if(!event.world.isRemote) {
			WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.world);
			if(worldHealer != null) {
				worldHealer.onTick();
			}
		}
	}
}
