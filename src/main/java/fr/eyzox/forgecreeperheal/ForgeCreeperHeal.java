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
import fr.eyzox.forgecreeperheal.reflection.ReflectionManager;
import fr.eyzox.forgecreeperheal.scheduler.TickTimelineFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
		modid = ForgeCreeperHeal.MODID,
		name = ForgeCreeperHeal.MODNAME,
		version = ForgeCreeperHeal.VERSION,
		acceptableRemoteVersions = "*"
		)
public class ForgeCreeperHeal
{
	
    public static final String MODID = "forgecreeperheal";
    public static final String VERSION = "2.0.1";
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

	public void registerReflection() {
		// For ChunkTransform
		ReflectionManager.getInstance().registerField(Chunk.class, "precipitationHeightMap", "field_76638_b");
		ReflectionManager.getInstance().registerMethod(Chunk.class, "relightBlock", "func_76615_h", new Class<?>[]{int.class, int.class, int.class});
		ReflectionManager.getInstance().registerMethod(Chunk.class, "propagateSkylightOcclusion", "func_76595_e", new Class<?>[]{int.class, int.class});
		
		// For WorldTransform
		ReflectionManager.getInstance().registerMethod(World.class, "isValid", "func_175701_a", new Class<?>[]{BlockPos.class});
		
		// For ExplosionEventHandler
		ReflectionManager.getInstance().registerField(Explosion.class, "exploder", "field_77283_e");
		
		// For I86n
		ReflectionManager.getInstance().registerField(EntityPlayerMP.class, "language", "field_71148_cg");

	}
}
