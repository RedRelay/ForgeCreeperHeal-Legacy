package fr.eyzox.forgecreeperheal;


import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import fr.eyzox.forgecreeperheal.commands.config.ConfigCommands;
import fr.eyzox.forgecreeperheal.commands.config.ReloadConfigCommand;
import fr.eyzox.forgecreeperheal.commands.profiler.ProfilerCommand;
import fr.eyzox.forgecreeperheal.handler.ExplosionEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldTickEventHandler;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

@Mod(
		modid = ForgeCreeperHeal.MODID,
		name = ForgeCreeperHeal.MODNAME,
		version = ForgeCreeperHeal.VERSION,
		acceptableRemoteVersions = "*"
		)
public class ForgeCreeperHeal
{
    public static final String MODID = "forgecreeperheal";
    public static final String VERSION = "1.0.0";
    public static final String MODNAME = "Forge Creeper Heal "+VERSION;
    
    @Instance(ForgeCreeperHeal.MODID)
	public static ForgeCreeperHeal instance;
    
    private Logger logger;
    
    private Config config;
    private WorldEventHandler worldEventHandler;
    
    
    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
    	this.logger = event.getModLog();
    	this.config = Config.loadConfig(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void onInit(FMLInitializationEvent event)
    {
    	this.worldEventHandler = new WorldEventHandler();
    	
    	FMLCommonHandler.instance().bus().register(new WorldTickEventHandler());
    	MinecraftForge.EVENT_BUS.register(worldEventHandler);
        MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
    }
    
    @EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		registerCommand();
	}
    
    private void registerCommand() {
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

	public static ForgeCreeperHeal getInstance() {
    	return instance;
    }
    
    public static Logger getLogger() {
    	return instance.logger;
    }
    
    public static WorldHealer getWorldHealer(WorldServer w) {
    	return instance.worldEventHandler.getWorldHealers().get(w);
    }
    
    public static Config getConfig() {
    	return instance.config;
    }
    
    public static void reloadConfig() {
    	instance.config = Config.loadConfig(instance.config.getConfigFile());
    }
}
