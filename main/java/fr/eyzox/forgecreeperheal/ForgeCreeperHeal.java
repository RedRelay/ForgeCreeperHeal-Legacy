package fr.eyzox.forgecreeperheal;


import net.minecraft.world.WorldServer;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;
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
    
    @SidedProxy(clientSide = "fr.eyzox.forgecreeperheal.proxy.ClientProxy", serverSide = "fr.eyzox.forgecreeperheal.proxy.CommonProxy")
	private static CommonProxy proxy;
    
    @Instance(ForgeCreeperHeal.MODID)
	public static ForgeCreeperHeal instance;
    
    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
    	proxy.onPreInit(event);
    }
    
    @EventHandler
    public void onInit(FMLInitializationEvent event) {
    	proxy.onInit(event);
    }
    
    @EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}

	public static ForgeCreeperHeal getInstance() {
    	return instance;
    }
    
    public static Logger getLogger() {
    	return proxy.getLogger();
    }
    
    public static WorldHealer getWorldHealer(WorldServer w) {
    	return proxy.getWorldEventHandler().getWorldHealers().get(w);
    }
    
    public static Config getConfig() {
    	return proxy.getConfig();
    }
    
    public static SimpleNetworkWrapper getChannel() {
    	return proxy.getChannel();
    }
    
    public static CommonProxy getProxy() {
    	return proxy;
    }
    public static void reloadConfig() {
    	proxy.setConfig(Config.loadConfig(instance.proxy.getConfig().getConfigFile()));
    }
}
