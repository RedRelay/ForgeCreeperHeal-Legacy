package fr.eyzox.forgecreeperheal.proxy;

import org.apache.logging.log4j.Logger;

import fr.eyzox.forgecreeperheal.Config;
import fr.eyzox.forgecreeperheal.builder.blockdata.BedBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.blockdata.DefaultBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.blockdata.DoorBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.blockdata.PistonBlockDataBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.AbstractGenericDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.FacingDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.IDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.LeverDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.NoDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.OppositeFacingDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.SupportByBottomDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.VineDependencyBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.property.BannerHangingPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.ButtonPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.CocoaPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.LadderPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.TorchPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.TrapDoorPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.TripWireHookPropertySelector;
import fr.eyzox.forgecreeperheal.builder.dependency.property.WallSignPropertySelector;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import fr.eyzox.forgecreeperheal.commands.HealCommand;
import fr.eyzox.forgecreeperheal.commands.config.ConfigCommands;
import fr.eyzox.forgecreeperheal.commands.config.ReloadConfigCommand;
import fr.eyzox.forgecreeperheal.factory.DefaultFactory;
import fr.eyzox.forgecreeperheal.factory.keybuilder.ClassKeyBuilder;
import fr.eyzox.forgecreeperheal.handler.ChunkEventHandler;
import fr.eyzox.forgecreeperheal.handler.ExplosionEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldTickEventHandler;
import fr.eyzox.forgecreeperheal.healer.HealerFactory;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner.BlockBannerHanging;
import net.minecraft.block.BlockBanner.BlockBannerStanding;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockWallSign;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

	private Logger logger;
    
    private Config config;
    //private SimpleNetworkWrapper channel;
    
    private HealerFactory healerFactory;
    private HealerManager healerManager;
    
    private DefaultFactory<Class<? extends Block>, IBlockDataBuilder> blockDataFactory;
    private DefaultFactory<Class<? extends Block>, IDependencyBuilder> dependencyFactory;
    private ClassKeyBuilder<Block> blockClassKeyBuilder;
    
    
    private ChunkEventHandler chunkEventHandler;
    private ExplosionEventHandler explosionEventHandler;
    private WorldTickEventHandler worldTickEventHandler;
    
    
	
	public void onPreInit(FMLPreInitializationEvent event) {
    	this.logger = event.getModLog();
    	this.config = Config.loadConfig(event.getSuggestedConfigurationFile());
    	//this.healGraphBuilder;
    }
	
	public void onInit(FMLInitializationEvent event)
    {	
		this.chunkEventHandler = new ChunkEventHandler();
		this.chunkEventHandler.register();
		
		this.explosionEventHandler = new ExplosionEventHandler();
		this.explosionEventHandler.register();
		
		this.worldTickEventHandler = new WorldTickEventHandler();
		this.worldTickEventHandler.register();
        
        //channel = NetworkRegistry.INSTANCE.newSimpleChannel(ForgeCreeperHeal.MODID+":"+"ch0");
        //channel.registerMessage(ModDataMessage.Handler.class, ModDataMessage.class, 0, Side.SERVER);
        //channel.registerMessage(ProfilerInfoMessage.Handler.class, ProfilerInfoMessage.class, 1, Side.CLIENT);
    }
	
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
		this.healerFactory = new HealerFactory();
    	this.healerManager = new HealerManager();
    	
    	this.blockClassKeyBuilder = new ClassKeyBuilder<Block>();
    	
    	this.blockDataFactory = new DefaultFactory<Class<? extends Block>, IBlockDataBuilder>(blockClassKeyBuilder, new DefaultBlockDataBuilder());
    	this.blockDataFactory.getCustomHandlers().add(new DoorBlockDataBuilder());
    	this.blockDataFactory.getCustomHandlers().add(new BedBlockDataBuilder());
    	this.blockDataFactory.getCustomHandlers().add(new PistonBlockDataBuilder());
    	
    	this.dependencyFactory = new DefaultFactory<Class<? extends Block>, IDependencyBuilder>(blockClassKeyBuilder, new NoDependencyBuilder());
    	this.dependencyFactory.getCustomHandlers().add(new VineDependencyBuilder());
    	this.dependencyFactory.getCustomHandlers().add(new LeverDependencyBuilder());
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockTorch.class, new TorchPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockLadder.class, new LadderPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockWallSign.class, new WallSignPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockTrapDoor.class, new TrapDoorPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockButton.class, new ButtonPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockBannerHanging.class, new BannerHangingPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockTripWireHook.class, new TripWireHookPropertySelector()));
    	this.dependencyFactory.getCustomHandlers().add(new FacingDependencyBuilder(BlockCocoa.class, new CocoaPropertySelector()));
    	
    	final AbstractGenericDependencyBuilder supportByBottomDependencyBuilder = SupportByBottomDependencyBuilder.getInstance();
    	supportByBottomDependencyBuilder.register(BlockFalling.class);
    	supportByBottomDependencyBuilder.register(BlockDoor.class);
    	supportByBottomDependencyBuilder.register(BlockBasePressurePlate.class);
    	supportByBottomDependencyBuilder.register(BlockBannerStanding.class);
    	supportByBottomDependencyBuilder.register(BlockRedstoneDiode.class);
    	supportByBottomDependencyBuilder.register(BlockRedstoneWire.class);
    	supportByBottomDependencyBuilder.register(BlockStandingSign.class);
    	supportByBottomDependencyBuilder.register(BlockCrops.class);
    	supportByBottomDependencyBuilder.register(BlockCactus.class);
    	supportByBottomDependencyBuilder.register(BlockRailBase.class);
    	supportByBottomDependencyBuilder.register(BlockReed.class);
    	supportByBottomDependencyBuilder.register(BlockSnow.class);
    	supportByBottomDependencyBuilder.register(BlockTripWire.class);
    	supportByBottomDependencyBuilder.register(BlockCake.class);
    	supportByBottomDependencyBuilder.register(BlockCarpet.class);
    	supportByBottomDependencyBuilder.register(BlockDragonEgg.class);
    	supportByBottomDependencyBuilder.register(BlockFlowerPot.class);
    	
    	this.dependencyFactory.getCustomHandlers().add(supportByBottomDependencyBuilder);
	}
    
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
    	//forgeCreeperHealCmds.register(new ProfilerCommand());
    	
    	//Register Heal Commands
    	forgeCreeperHealCmds.register(new HealCommand());
    	
		m.registerCommand(forgeCreeperHealCmds);
	}

	public Logger getLogger() {
		return logger;
	}

	public Config getConfig() {
		return config;
	}

	/*
	public SimpleNetworkWrapper getChannel() {
		return channel;
	}
	*/

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public HealerManager getHealerManager() {
		return healerManager;
	}
	
	public HealerFactory getHealerFactory() {
		return healerFactory;
	}
	
	public DefaultFactory<Class<? extends Block>, IBlockDataBuilder> getBlockDataFactory() {
		return blockDataFactory;
	}
	
	public DefaultFactory<Class<? extends Block>, IDependencyBuilder> getDependencyFactory() {
		return dependencyFactory;
	}
	
	public ChunkEventHandler getChunkEventHandler() {
		return chunkEventHandler;
	}
	
	public ClassKeyBuilder<Block> getBlockClassKeyBuilder() {
		return blockClassKeyBuilder;
	}
	
}
