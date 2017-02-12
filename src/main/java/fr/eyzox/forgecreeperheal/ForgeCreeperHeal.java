package fr.eyzox.forgecreeperheal;


import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;
import fr.eyzox.forgecreeperheal.commands.ProfilerCommand;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

@Mod(
		modid = ForgeCreeperHeal.MODID,
		name = ForgeCreeperHeal.MODNAME,
		version = ForgeCreeperHeal.VERSION,
		acceptableRemoteVersions = "*",
		guiFactory = "fr.eyzox." + ForgeCreeperHeal.MODID + ".IngameConfigFactory"
		)
public class ForgeCreeperHeal{
	
    public static final String MODID = "forgecreeperheal";
    public static final String VERSION = "1.3.0";
    public static final String MODNAME = "Forge Creeper Heal";
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

    	MinecraftForge.EVENT_BUS.register(instance);
    }
	
    @EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
 		event.registerServerCommand(new ProfilerCommand()); 
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

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.getModID().equals(ForgeCreeperHeal.MODID)) {
			ForgeCreeperHeal.getConfig().syncConfig();
		}
	}
}
