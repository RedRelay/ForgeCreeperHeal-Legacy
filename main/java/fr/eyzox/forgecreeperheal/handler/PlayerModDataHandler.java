package fr.eyzox.forgecreeperheal.handler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.network.ModDataMessage;

public class PlayerModDataHandler {

	@SubscribeEvent

	public void onLogged(EntityJoinWorldEvent event) {
		if(event.getEntity() == Minecraft.getMinecraft().thePlayer) {
			ForgeCreeperHeal.getChannel().sendToServer(new ModDataMessage().fill());
		}
	}
}
