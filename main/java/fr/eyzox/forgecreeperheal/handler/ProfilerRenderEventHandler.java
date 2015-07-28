package fr.eyzox.forgecreeperheal.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.eyzox.forgecreeperheal.network.profiler.ProfilerInfoMessage;

@SideOnly(Side.CLIENT)
public class ProfilerRenderEventHandler {

	private Map<World, ProfilerInfoMessage> map = new HashMap<World, ProfilerInfoMessage>();

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
		if(!Minecraft.getMinecraft().isGamePaused()) {
			for(ProfilerInfoMessage msg : map.values()) {
				event.left.add(getDrawString(msg));
				msg.setDisplayTicks(msg.getDisplayTicks()+1);
				if(msg.getDisplayTicks() > 40) {
					map.remove(msg.getWorld());
				}
			}
		}
	}

	public void onMessage(ProfilerInfoMessage msg) {
		map.put(msg.getWorld(), msg);
	}
	
	private String getDrawString(ProfilerInfoMessage msg) {
		return String.format("[%s#%d] Ticks : %.4f ms | Memory Usage : %d blocks", msg.getWorld() == null? null : msg.getWorld().getWorldInfo().getWorldName(),  msg.getWorld() == null? 0 : msg.getWorld().provider.dimensionId, msg.getTicks(), msg.getBlocksUsed());
	}


}
