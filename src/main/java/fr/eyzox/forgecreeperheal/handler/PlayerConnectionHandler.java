package fr.eyzox.forgecreeperheal.handler;

import fr.eyzox.forgecreeperheal.i18n.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerConnectionHandler implements IEventHandler{


	@SubscribeEvent
	public void onPlayerLeave(PlayerLoggedOutEvent evt) {
		if(evt.player instanceof EntityPlayerMP) {
			I18n.getInstance().onPlayerLeave((EntityPlayerMP) evt.player);
		}
	}
	
	@Override
	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

}
