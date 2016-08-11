package fr.eyzox.forgecreeperheal.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.bsc.config.Config;
import fr.eyzox.bsc.config.ConfigOptionGroup;
import fr.eyzox.bsc.config.IConfigListener;
import fr.eyzox.bsc.config.IConfigProvider;
import fr.eyzox.bsc.config.loader.IConfigLoader;
import fr.eyzox.bsc.config.option.ConfigOption;
import fr.eyzox.bsc.config.option.ConfigOptionList;
import fr.eyzox.bsc.config.option.validator.BooleanValidator;
import fr.eyzox.bsc.config.option.validator.IValidator;
import fr.eyzox.bsc.config.option.validator.IntValidator;
import fr.eyzox.bsc.exception.ConfigException;
import fr.eyzox.bsc.exception.InvalidValueException;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.init.Blocks;

public class ConfigProvider implements IConfigProvider{

	public static final String GROUP_HEALING_TIME = "healing time";
	public static final String GROUP_OVERRIDE = "override";
	public static final String GROUP_CONTAINERS = "containers";
	public static final String GROUP_FILTERS = "filters";
	
	public static final String OPTION_MIN_TICK_BEFORE_HEAL = "minTickStart";
	public static final String OPTION_MAX_TICK_BEFORE_HEAL = "maxTickStart";
	public static final String OPTION_MIN_TICK = "minTick";
	public static final String OPTION_MAX_TICK = "maxTick";
	public static final String OPTION_OVERRIDE_BLOCK = "overrideBlock";
	public static final String OPTION_OVERRIDE_FUILD = "overrideFluid";
	public static final String OPTION_DROP_IF_COLLISION = "dropIfCollision";
	public static final String OPTION_DROP_ITEMS = "dropItems";
	public static final String OPTION_REMOVE_EXCEPTION = "removeException";
	public static final String OPTION_HEAL_EXCEPTION = "healException";
	public static final String OPTION_SOURCE_EXCEPTION = "sourceException";
	
	private final List<IConfigListener> listeners = new LinkedList<IConfigListener>();

	private IConfigLoader configLoader;
	private File errorFile;
	
	private Config config;

	public ConfigProvider() {
		this(null);
	}

	public ConfigProvider(final IConfigLoader loader) {
		this(loader, null);
	}
	
	public ConfigProvider (final IConfigLoader loader, final File errorFile) {
		this.configLoader = loader;
		this.errorFile = errorFile;
	}

	@Override
	public synchronized Config getConfig() throws ConfigException{
		if(config == null) {
			throw new ConfigException("Config is not loaded");
		}
		return config;
	}

	@Override
	public synchronized void addConfigListener(IConfigListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public synchronized void removeConfigListener(IConfigListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public synchronized void loadConfig() {
		config = loadDefaultConfig();
		if(configLoader != null) {
			try {
				this.configLoader.load(config);
			}catch(final NoSuchFileException e) {
				ForgeCreeperHeal.getLogger().info("Config File doesn't exist at "+e.getFile()+" : creating a new one");
			}catch (Exception e2) {
				e2.printStackTrace();
				ForgeCreeperHeal.getLogger().error("Unable to load config : "+e2.getMessage());
			}
		}
		
		if(configLoader.getErrorManager().hasErrors()) {
    		FileWriter out = null;
    		try {
    			out = new FileWriter(errorFile);
    			PrintWriter pw = new PrintWriter(out);
    			configLoader.getErrorManager().output(pw);
    			ForgeCreeperHeal.getLogger().warn("Errors occurs during loading config, more information in "+errorFile.getAbsolutePath());
    		}catch(IOException e) {
    			ForgeCreeperHeal.getLogger().error("Errors occurs during loading config, unable to write into "+errorFile.getAbsolutePath()+" : "+e.getMessage());
    			for(ConfigException error : configLoader.getErrorManager().getErrors()) {
    				ForgeCreeperHeal.getLogger().info(error.getMessage());
    			}
    		}finally {
				if(out != null) {
					try {
						out.close();
					} catch (IOException e) {}
				}
			}
    	}

	}

	@Override
	public synchronized void unloadConfig() {
		config = null;
	}
	
	@Override
	public synchronized void fireConfigChanged() throws ConfigException{
		final Config config = this.getConfig();
		for(IConfigListener listener : listeners) {
			listener.onChange(config);
		}
	}
	
	public synchronized IConfigLoader getConfigLoader() {
		return configLoader;
	}
	
	public synchronized void setConfigLoader(IConfigLoader configLoader) {
		this.configLoader = configLoader;
	}
	
	public synchronized File getErrorFile() {
		return errorFile;
	}
	
	public synchronized void setErrorFile(File errorFile) {
		this.errorFile = errorFile;
	}

	private Config loadDefaultConfig() {

		final IntValidator positiveIntValidator = new IntValidator(0, Integer.MAX_VALUE);

		//## ConfigOptions

		final ConfigOption minTickBeforeStartHeal = new ConfigOption(OPTION_MIN_TICK_BEFORE_HEAL);
		final ConfigOption maxTickBeforeStartHeal = new ConfigOption(OPTION_MAX_TICK_BEFORE_HEAL);
		final ConfigOption minTickBetweenEachHeal = new ConfigOption(OPTION_MIN_TICK);
		final ConfigOption maxTickBetweenEachHeal = new ConfigOption(OPTION_MAX_TICK);

		final ConfigOption overrideBlock = new ConfigOption(OPTION_OVERRIDE_BLOCK);
		final ConfigOption overrideFluid = new ConfigOption(OPTION_OVERRIDE_FUILD);
		final ConfigOption dropIfAlreadyBlock = new ConfigOption(OPTION_DROP_IF_COLLISION);

		final ConfigOption dropItemsFromContainers = new ConfigOption(OPTION_DROP_ITEMS);

		final ConfigOptionList removeException = new ConfigOptionList(OPTION_REMOVE_EXCEPTION);
		final ConfigOptionList healException = new ConfigOptionList(OPTION_HEAL_EXCEPTION);
		final ConfigOptionList fromEntityException = new ConfigOptionList(OPTION_SOURCE_EXCEPTION);

		//## Default value

		//6000 ticks = 360 seconds = 5 mins = 1/2 minecraft day
		//12000 ticks = 720 seconds = 10 mins = 1 minecraft day
		minTickBeforeStartHeal.setValue("6000");
		maxTickBeforeStartHeal.setValue("12000");
		//200 ticks = 10 seconds
		minTickBetweenEachHeal.setValue("0");
		maxTickBetweenEachHeal.setValue("200");

		overrideBlock.setValue("false");
		overrideFluid.setValue("true");
		dropIfAlreadyBlock.setValue("true");

		dropItemsFromContainers.setValue("false");

		final List<String> removeExceptionValues = new ArrayList<String>(1);
		removeExceptionValues.add(Blocks.TNT.getRegistryName().toString());
		removeException.setValues(removeExceptionValues);
		final List<String> healExceptionValues = new ArrayList<String>(1);
		healExceptionValues.add(Blocks.TNT.getRegistryName().toString());
		healException.setValues(healExceptionValues);
		final List<String> fromEntityExceptionValues = new ArrayList<String>();
		fromEntityException.setValues(fromEntityExceptionValues);

		//## Validator

		minTickBeforeStartHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidValueException {
				if(positiveIntValidator.isValid(value)) {
					if(Integer.parseInt(value) <= Integer.parseInt(maxTickBeforeStartHeal.getValue())) {
						return true;
					}else {
						throw new InvalidValueException(value, String.format("INTEGER <= %s [%s]", maxTickBeforeStartHeal.getName(), maxTickBeforeStartHeal.getValue()));
					}
				}else {
					return false;
				}
			}
		});

		maxTickBeforeStartHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidValueException {
				if(positiveIntValidator.isValid(value)) {
					if(Integer.parseInt(minTickBeforeStartHeal.getValue()) <= Integer.parseInt(value)) {
						return true;
					}else {
						throw new InvalidValueException(value, String.format("INTEGER >= %s [%s]", minTickBeforeStartHeal.getName(), minTickBeforeStartHeal.getValue()));
					}
				}else {
					return false;
				}
			}
		});

		minTickBetweenEachHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidValueException {
				if(positiveIntValidator.isValid(value)) {
					if(Integer.parseInt(value) <= Integer.parseInt(maxTickBetweenEachHeal.getValue())) {
						return true;
					}else {
						throw new InvalidValueException(value, String.format("INTEGER <= %s [%s]", maxTickBetweenEachHeal.getName(), maxTickBetweenEachHeal.getValue()));
					}
				}else {
					return false;
				}
			}
		});

		maxTickBetweenEachHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidValueException {
				if(positiveIntValidator.isValid(value)) {
					if(Integer.parseInt(minTickBetweenEachHeal.getValue()) <= Integer.parseInt(value)) {
						return true;
					}else {
						throw new InvalidValueException(value, String.format("INTEGER >= %s [%s]", minTickBetweenEachHeal.getName(), minTickBetweenEachHeal.getValue()));
					}
				}else {
					return false;
				}
			}
		});


		overrideBlock.setValidator(BooleanValidator.getInstance());
		overrideFluid.setValidator(BooleanValidator.getInstance());
		dropIfAlreadyBlock.setValidator(BooleanValidator.getInstance());

		dropItemsFromContainers.setValidator(BooleanValidator.getInstance());

		//## Descriptions

		minTickBeforeStartHeal.setDescription("Minimum ticks before starting heal (Only affect new explosions)");
		maxTickBeforeStartHeal.setDescription("Maximum ticks before starting heal (Only affect new explosions)");
		minTickBetweenEachHeal.setDescription("Minimum ticks between each heal (Only affect new explosions)");
		maxTickBetweenEachHeal.setDescription("Maximum ticks between each heal (Only affect new explosions)");

		overrideBlock.setDescription("When a block is healed, if a replacement block has been placed meanwhile, this block is overrided by the healed block");
		overrideFluid.setDescription("When a block is healed, if a fluid filled the block position meanwhile, this fluid is overrided at this position by the healed block");
		dropIfAlreadyBlock.setDescription("If "+overrideBlock.getName()+" then the replacement block is dropped, else the block about to be heal is dropped");

		dropItemsFromContainers.setDescription("Drops all items from a container when it blows up, else items are restored with the container when healed (Only affect new explosions)");

		removeException.setDescription("These blocks will be not removed after a handled explosion and will be affected by the explosion");
		healException.setDescription("These blocks will be not healed");
		fromEntityException.setDescription("Forge Creeper Heal does not handle explosion triggered by entity from these entities");

		//## ConfigOptionGroups

		final ConfigOptionGroup healingTimeGroup = new ConfigOptionGroup(GROUP_HEALING_TIME);
		healingTimeGroup.addOption(minTickBeforeStartHeal);
		healingTimeGroup.addOption(maxTickBeforeStartHeal);
		healingTimeGroup.addOption(minTickBetweenEachHeal);
		healingTimeGroup.addOption(maxTickBetweenEachHeal);

		final ConfigOptionGroup overrideGroup = new ConfigOptionGroup(GROUP_OVERRIDE);
		overrideGroup.addOption(overrideBlock);
		overrideGroup.addOption(overrideFluid);
		overrideGroup.addOption(dropIfAlreadyBlock);

		final ConfigOptionGroup containerGroup = new ConfigOptionGroup(GROUP_CONTAINERS);
		containerGroup.addOption(dropItemsFromContainers);

		final ConfigOptionGroup filtersGroup =  new ConfigOptionGroup(GROUP_FILTERS);
		filtersGroup.addOption(removeException);
		filtersGroup.addOption(healException);
		filtersGroup.addOption(fromEntityException);

		//## Config

		final Config config = new Config();
		config.addOptionGroup(healingTimeGroup);
		config.addOptionGroup(overrideGroup);
		config.addOptionGroup(containerGroup);
		config.addOptionGroup(filtersGroup);
		return config;
	}

}
