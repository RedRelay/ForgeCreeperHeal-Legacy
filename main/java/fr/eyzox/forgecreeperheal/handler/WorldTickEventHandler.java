package fr.eyzox.forgecreeperheal.handler;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import fr.eyzox.forgecreeperheal.serial.ISerializableHealable;
import fr.eyzox.ticktimeline.TickTimeline;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldTickEventHandler implements IEventHandler{

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {

		if(event.world.isRemote || event.phase != TickEvent.Phase.START) return;

		final WorldServer world = (WorldServer) event.world;

		final HealerManager healerMananger = ForgeCreeperHeal.getHealerManager();
		final Map<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> loadedHealers = healerMananger.getLoadedHealers(world);

		if(loadedHealers != null) {
			for(final Entry<ChunkCoordIntPair, TickTimeline<ISerializableHealable>> entry : loadedHealers.entrySet()) {
				final Collection<ISerializableHealable> healables = entry.getValue().tick();
				if(healables != null) {
					for(final ISerializableHealable healable : healables) {
						healable.heal(world, 7);
					}

					if(entry.getValue().isEmpty()) {
						healerMananger.removeHealer(world, entry.getKey());
					}

				}
			}
		}


	}

	@Override
	public void register() {
		FMLCommonHandler.instance().bus().register(this);
	}
}
