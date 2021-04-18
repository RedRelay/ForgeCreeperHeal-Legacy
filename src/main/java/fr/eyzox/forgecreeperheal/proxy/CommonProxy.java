package fr.eyzox.forgecreeperheal.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Logger;
import fr.eyzox.forgecreeperheal.Config;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;

public class CommonProxy {

  //renderer only for client? 
  private Logger logger;
  public WorldEventHandler worldEventHandler;
  public Config config = new Config();
  private static final String PROTOCOL_VERSION = Integer.toString(1);
  public static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
      .named(new ResourceLocation(ForgeCreeperHeal.MODID, "main_channel"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();
  //  public void onPreInit(FMLPreInitializationEvent event) {
  //    this.logger = event.getModLog();
  //    this.config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
  //  }

  public static void addChatMessage(PlayerEntity sender, TranslationTextComponent msg) {
    sender.sendMessage(msg, sender.getUniqueID());
  }

  public static void addChatMessage(PlayerEntity sender, String string) {
    sender.sendMessage(new TranslationTextComponent(string), sender.getUniqueID());
  }

  public Logger getLogger() {
    return logger;
  }

  public WorldEventHandler getWorldEventHandler() {
    return worldEventHandler;
  }

  public SimpleChannel getChannel() {
    return channel;
  }
}
