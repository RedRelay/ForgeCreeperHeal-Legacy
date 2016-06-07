package fr.eyzox.forgecreeperheal.proxy;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import fr.eyzox.forgecreeperheal.Config;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.handler.ExplosionEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldTickEventHandler;
import fr.eyzox.forgecreeperheal.network.ProfilerInfoMessage;

public class CommonProxy {

	private Logger logger;
    private Config config;
    private WorldEventHandler worldEventHandler;
    private SimpleNetworkWrapper channel;
	
	public void onPreInit(FMLPreInitializationEvent event) {
    	this.logger = event.getModLog();
    	this.config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
    }
	
	public void onInit(FMLInitializationEvent event){
    	this.worldEventHandler = new WorldEventHandler();
    	
    	MinecraftForge.EVENT_BUS.register(new WorldTickEventHandler());
    	MinecraftForge.EVENT_BUS.register(worldEventHandler);
        MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
        
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(ForgeCreeperHeal.MODID+":"+"ch0");
        channel.registerMessage(ProfilerInfoMessage.Handler.class, ProfilerInfoMessage.class, 1, Side.CLIENT);
    }

    public static void addChatMessage(ICommandSender sender, TextComponentTranslation msg) {
		TextComponentTranslation cct = new TextComponentTranslation(String.format("[%s] ", ForgeCreeperHeal.MODNAME));
		cct.appendSibling(msg);
		sender.addChatMessage(cct);
	}

	public static void addChatMessage(ICommandSender sender, String string) {
		addChatMessage(sender,new TextComponentTranslation(I18n.format(string)));
	}
   
	public Logger getLogger() {
		return logger;
	}

	public Config getConfig() {
		return config;
	}

	public WorldEventHandler getWorldEventHandler() {
		return worldEventHandler;
	}

	public SimpleNetworkWrapper getChannel() {
		return channel;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
