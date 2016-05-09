package fr.eyzox.forgecreeperheal;


import org.apache.logging.log4j.Logger;

import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.IDependencyBuilder;
import fr.eyzox.forgecreeperheal.factory.DefaultFactory;
import fr.eyzox.forgecreeperheal.healer.HealerFactory;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

//TODO config + test serial
/*
 * Planned feature :
 * - GUI to configure options
 * - Better config for Entity Filter : idea : use EntityRegistry to get <ModContainer.getModId()>:<EntityRegistration.entityName>
 * 
 * Known Bugs/Issues :
 * - Banner don't keep original color, they become black when healed
 */

@Mod(
		modid = ForgeCreeperHeal.MODID,
		name = ForgeCreeperHeal.MODNAME,
		version = ForgeCreeperHeal.VERSION,
		acceptableRemoteVersions = "*"
		)
public class ForgeCreeperHeal
{
	
    public static final String MODID = "forgecreeperheal";
    public static final String VERSION = "2.0.0";
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
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    	proxy.serverAboutToStart(event);
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
    
    public static Config getConfig() {
    	//TODO this is the old config system
    	return new Config();
    }
    
    /*
    public static SimpleNetworkWrapper getChannel() {
    	return proxy.getChannel();
    }
    */
    
    public static CommonProxy getProxy() {
    	return proxy;
    }
    
    public static void reloadConfig() {
    	//TODO reloadConfig
    	//proxy.setConfig(Config.loadConfig(instance.proxy.getConfig().getConfigFile()));
    }
    
    public static HealerManager getHealerManager() {
    	return proxy.getHealerManager();
    }
    
    public static HealerFactory getHealerFactory() {
    	return proxy.getHealerFactory();
    }
    
    public static DefaultFactory<Class<? extends Block>, IBlockDataBuilder> getBlockDataFactory() {
    	return proxy.getBlockDataFactory();
    }
    
    public static DefaultFactory<Class<? extends Block>, IDependencyBuilder> getDependencyFactory() {
    	return proxy.getDependencyFactory();
    }
}
