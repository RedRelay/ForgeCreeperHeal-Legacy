package fr.eyzox.forgecreeperheal.proxy;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import fr.eyzox.forgecreeperheal.Config;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import fr.eyzox.forgecreeperheal.commands.config.ConfigCommands;
import fr.eyzox.forgecreeperheal.commands.config.ReloadConfigCommand;
import fr.eyzox.forgecreeperheal.commands.profiler.ProfilerCommand;
import fr.eyzox.forgecreeperheal.handler.ExplosionEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldTickEventHandler;
import fr.eyzox.forgecreeperheal.network.ModDataMessage;
import fr.eyzox.forgecreeperheal.network.ProfilerInfoMessage;

public class CommonProxy {

	private Logger logger;
    
    private Config config;
    private WorldEventHandler worldEventHandler;
    private SimpleNetworkWrapper channel;
	
	public void onPreInit(FMLPreInitializationEvent event) {
    	this.logger = event.getModLog();
    	this.config = Config.loadConfig(event.getSuggestedConfigurationFile());
    }
	
	public void onInit(FMLInitializationEvent event)
    {
    	this.worldEventHandler = new WorldEventHandler();
    	
    	FMLCommonHandler.instance().bus().register(new WorldTickEventHandler());
    	MinecraftForge.EVENT_BUS.register(worldEventHandler);
        MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
        
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(ForgeCreeperHeal.MODID+":"+"ch0");
        channel.registerMessage(ModDataMessage.Handler.class, ModDataMessage.class, 0, Side.SERVER);
        channel.registerMessage(ProfilerInfoMessage.Handler.class, ProfilerInfoMessage.class, 1, Side.CLIENT);
    }
    
    @EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		registerCommand();
	}
    
    protected void registerCommand() {
    	ServerCommandManager m = (ServerCommandManager) MinecraftServer.getServer().getCommandManager();
    	
    	ForgeCreeperHealCommands forgeCreeperHealCmds = new ForgeCreeperHealCommands();
    	
    	//Register Config Commands
    	ConfigCommands configCmds = new ConfigCommands();
    	configCmds.register(new ReloadConfigCommand());
    	forgeCreeperHealCmds.register(configCmds);
    	
    	//Register Profiler Commands
    	forgeCreeperHealCmds.register(new ProfilerCommand());
    	
		m.registerCommand(forgeCreeperHealCmds);
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
