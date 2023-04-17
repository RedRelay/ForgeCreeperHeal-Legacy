package com.lothrazar.creeperheal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.creeperheal.handler.ExplosionEventHandler;
import com.lothrazar.creeperheal.handler.WorldEventHandler;
import com.lothrazar.creeperheal.handler.WorldTickEventHandler;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ForgeCreeperHeal.MODID)
public class ForgeCreeperHeal {

  public static final String MODID = "creeperheal";
  public static final Logger LOGGER = LogManager.getLogger();
  private static WorldEventHandler WEV;

  public ForgeCreeperHeal() {
    new ConfigRegistryCreeperheal();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    ForgeCreeperHeal.WEV = new WorldEventHandler();
  }

  private void setup(final FMLCommonSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(new WorldTickEventHandler());
    MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
    MinecraftForge.EVENT_BUS.register(WEV);
  }

  public static WorldHealer getWorldHealer(ServerLevel level) {
    return WEV.getWorldHealers().get(level);
  }
}
