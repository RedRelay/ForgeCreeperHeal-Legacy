package fr.eyzox.forgecreeperheal;


import java.util.Map;

import org.apache.logging.log4j.Logger;

import fr.eyzox.bsc.config.IConfigProvider;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.IDependencyBuilder;
import fr.eyzox.forgecreeperheal.config.FastConfig;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;
import fr.eyzox.forgecreeperheal.factory.DefaultFactory;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;
import fr.eyzox.forgecreeperheal.scheduler.TickTimelineFactory;
import net.minecraft.block.Block;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/*
 * Planned feature :
 * - GUI to configure options
 * - Better config for Entity Filter : idea : use EntityRegistry to get <ModContainer.getModId()>:<EntityRegistration.entityName>
 * 
 * Known Bugs/Issues :
 * - See https://github.com/RedRelay/ForgeCreeperHeal/issues
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
    
    public static IConfigProvider getConfigProvider() {
    	return proxy.getConfigProvider();
    }
    
    public static FastConfig getConfig() {
    	return proxy.getConfig();
    }
    
    /*
    public static SimpleNetworkWrapper getChannel() {
    	return proxy.getChannel();
    }
    */
    
    public static CommonProxy getProxy() {
    	return proxy;
    }
    
    public static HealerManager getHealerManager(final WorldServer world) throws ForgeCreeperHealException {
    	Map<WorldServer, HealerManager> healerManagers = proxy.getHealerManagers();
    	if(healerManagers == null) {
			throw new ForgeCreeperHealException("HealerManagers are not loaded yet");
		}
    	return healerManagers.get(world);
    }
    
    public static TickTimelineFactory getHealerFactory() {
    	return proxy.getHealerFactory();
    }
    
    public static DefaultFactory<Block, IBlockDataBuilder> getBlockDataFactory() {
    	return proxy.getBlockDataFactory();
    }
    
    public static DefaultFactory<Block, IDependencyBuilder> getDependencyFactory() {
    	return proxy.getDependencyFactory();
    }
}
