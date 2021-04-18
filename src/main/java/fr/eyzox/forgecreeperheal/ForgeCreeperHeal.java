package fr.eyzox.forgecreeperheal;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.eyzox.forgecreeperheal.handler.ExplosionEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldTickEventHandler;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

@Mod(ForgeCreeperHeal.MODID)
public class ForgeCreeperHeal {

  public static final String MODID = "forgecreeperheal";
  public static CommonProxy proxy = new CommonProxy();
  public static Logger logger = LogManager.getLogger();

  public ForgeCreeperHeal() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    ConfigRegistry.setup(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
  }

  private void setupClient(final FMLClientSetupEvent event) {}

  private void setup(final FMLCommonSetupEvent event) {
    proxy.worldEventHandler = new WorldEventHandler();
    MinecraftForge.EVENT_BUS.register(new WorldTickEventHandler());
    MinecraftForge.EVENT_BUS.register(proxy.worldEventHandler);
    MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
  }

  public static WorldHealer getWorldHealer(ServerWorld w) {
    return proxy.getWorldEventHandler().getWorldHealers().get(w);
  }
}
