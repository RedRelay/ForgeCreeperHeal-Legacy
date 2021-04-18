package com.lothrazar.creeperheal;

import com.lothrazar.creeperheal.handler.ExplosionEventHandler;
import com.lothrazar.creeperheal.handler.WorldEventHandler;
import com.lothrazar.creeperheal.handler.WorldTickEventHandler;
import com.lothrazar.creeperheal.worldhealer.WorldHealer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ForgeCreeperHeal.MODID)
public class ForgeCreeperHeal {

  public static final String MODID = "creeperheal";
  public static final Logger LOGGER = LogManager.getLogger();
  private static WorldEventHandler WEV;

  public ForgeCreeperHeal() {
    ForgeCreeperHeal.WEV = new WorldEventHandler();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    ConfigRegistry.setup(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }

  private void setupClient(final FMLClientSetupEvent event) {}

  private void setup(final FMLCommonSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(new WorldTickEventHandler());
    MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
    MinecraftForge.EVENT_BUS.register(WEV);
  }

  public static WorldHealer getWorldHealer(ServerWorld w) {
    return WEV.getWorldHealers().get(w);
  }
}
