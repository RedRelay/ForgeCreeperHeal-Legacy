package fr.eyzox.forgecreeperheal.proxy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import fr.eyzox.bsc.config.IConfigProvider;
import fr.eyzox.bsc.config.loader.JSONConfigLoader;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
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
import fr.eyzox.forgecreeperheal.config.ConfigProvider;
import fr.eyzox.forgecreeperheal.config.FastConfig;
import fr.eyzox.forgecreeperheal.factory.DefaultFactory;
import fr.eyzox.forgecreeperheal.factory.keybuilder.ClassKeyBuilder;
import fr.eyzox.forgecreeperheal.handler.ChunkEventHandler;
import fr.eyzox.forgecreeperheal.handler.ExplosionEventHandler;
import fr.eyzox.forgecreeperheal.handler.WorldEventHandler;
import fr.eyzox.forgecreeperheal.healer.HealerFactory;
import fr.eyzox.forgecreeperheal.healer.HealerManager;
import fr.eyzox.forgecreeperheal.serial.TimelineSerializer;
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
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockWallSign;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

	private Logger logger;

	private ConfigProvider configProvider;
	private FastConfig config;
	//private SimpleNetworkWrapper channel;

	private HealerFactory healerFactory;
	private Map<WorldServer, HealerManager> healerManagers;

	private DefaultFactory<Class<? extends Block>, IBlockDataBuilder> blockDataFactory;
	private DefaultFactory<Class<? extends Block>, IDependencyBuilder> dependencyFactory;
	private ClassKeyBuilder<Block> blockClassKeyBuilder;

	private TimelineSerializer timelineSerializer;

	private ChunkEventHandler chunkEventHandler;
	private ExplosionEventHandler explosionEventHandler;
	private WorldEventHandler worldTickEventHandler;



	public void onPreInit(FMLPreInitializationEvent event) {
		this.logger = event.getModLog();
		this.configProvider = new ConfigProvider(new JSONConfigLoader(event.getSuggestedConfigurationFile()), new File(ForgeCreeperHeal.MODID+"-config-error.log"));

		this.healerFactory = new HealerFactory();
		this.blockClassKeyBuilder = new ClassKeyBuilder<Block>();
		this.blockDataFactory = loadBlockDataFactory();
		this.dependencyFactory = loadDependencyFactory();
		
		this.timelineSerializer = new TimelineSerializer();

		this.config = new FastConfig();
		this.configProvider.addConfigListener(config);
		this.loadConfig();

	}

	public void onInit(FMLInitializationEvent event)
	{	
		this.chunkEventHandler = new ChunkEventHandler();
		this.chunkEventHandler.register();

		this.explosionEventHandler = new ExplosionEventHandler();
		this.explosionEventHandler.register();

		this.worldTickEventHandler = new WorldEventHandler();
		this.worldTickEventHandler.register();

		//channel = NetworkRegistry.INSTANCE.newSimpleChannel(ForgeCreeperHeal.MODID+":"+"ch0");
		//channel.registerMessage(ModDataMessage.Handler.class, ModDataMessage.class, 0, Side.SERVER);
		//channel.registerMessage(ProfilerInfoMessage.Handler.class, ProfilerInfoMessage.class, 1, Side.CLIENT);
	}

	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
		this.healerManagers = new HashMap<WorldServer, HealerManager>();
	}

	public void serverStarting(FMLServerStartingEvent event) {
		registerCommand();
	}

	protected void registerCommand() {
		ServerCommandManager m = (ServerCommandManager) FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
		ForgeCreeperHealCommands forgeCreeperHealCmds = new ForgeCreeperHealCommands();
		m.registerCommand(forgeCreeperHealCmds);
	}

	public Logger getLogger() {
		return logger;
	}

	public IConfigProvider getConfigProvider() {
		return configProvider;
	}

	public FastConfig getConfig() {
		return this.config;
	}

	/*
	public SimpleNetworkWrapper getChannel() {
		return channel;
	}
	 */

	public Map<WorldServer, HealerManager> getHealerManagers(){
		return healerManagers;
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
	
	public TimelineSerializer getTimelineSerializer() {
		return timelineSerializer;
	}

	public void loadConfig() {
		this.configProvider.loadConfig();

		//Save config to have a clean config file (add new options or suppress old options / mistake from user)
		try {
			this.configProvider.getConfigLoader().save(this.configProvider.getConfig());
		} catch (Exception e) {
			e.printStackTrace();
			ForgeCreeperHeal.getLogger().error("Unable to save config : "+e.getMessage());
		}

		//Notify listener to update
		this.configProvider.fireConfigChanged();
		//Unload config to free some RAM
		this.configProvider.unloadConfig();
	}

	private DefaultFactory<Class<? extends Block>, IBlockDataBuilder> loadBlockDataFactory() {
		final DefaultFactory<Class<? extends Block>, IBlockDataBuilder> blockDataFactory = new DefaultFactory<Class<? extends Block>, IBlockDataBuilder>(blockClassKeyBuilder, new DefaultBlockDataBuilder());
		blockDataFactory.getCustomHandlers().add(new DoorBlockDataBuilder());
		blockDataFactory.getCustomHandlers().add(new BedBlockDataBuilder());
		blockDataFactory.getCustomHandlers().add(new PistonBlockDataBuilder());
		return blockDataFactory;
	}

	private DefaultFactory<Class<? extends Block>, IDependencyBuilder> loadDependencyFactory() {
		final DefaultFactory<Class<? extends Block>, IDependencyBuilder> dependencyFactory = new DefaultFactory<Class<? extends Block>, IDependencyBuilder>(blockClassKeyBuilder, new NoDependencyBuilder());
		dependencyFactory.getCustomHandlers().add(new VineDependencyBuilder());
		dependencyFactory.getCustomHandlers().add(new LeverDependencyBuilder());
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockTorch.class, new TorchPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockLadder.class, new LadderPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockWallSign.class, new WallSignPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockTrapDoor.class, new TrapDoorPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockButton.class, new ButtonPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockBannerHanging.class, new BannerHangingPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new OppositeFacingDependencyBuilder(BlockTripWireHook.class, new TripWireHookPropertySelector()));
		dependencyFactory.getCustomHandlers().add(new FacingDependencyBuilder(BlockCocoa.class, new CocoaPropertySelector()));

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
		supportByBottomDependencyBuilder.register(BlockGrass.class);
		supportByBottomDependencyBuilder.register(BlockTallGrass.class);

		dependencyFactory.getCustomHandlers().add(supportByBottomDependencyBuilder);

		return dependencyFactory;
	}

}
