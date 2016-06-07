package fr.eyzox.forgecreeperheal;


import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.apache.logging.log4j.Logger;

import fr.eyzox.forgecreeperheal.commands.ProfilerCommand;
import fr.eyzox.forgecreeperheal.commands.ReloadConfigCommand;
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
    public static final String VERSION = "1.1.1";
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
 		event.registerServerCommand(new ProfilerCommand()); 
 		event.registerServerCommand(new ReloadConfigCommand()); 
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
}
