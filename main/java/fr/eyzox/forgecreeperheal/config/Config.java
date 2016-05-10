package fr.eyzox.forgecreeperheal.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.eyzox.forgecreeperheal.config.option.ConfigOption;
import fr.eyzox.forgecreeperheal.config.option.ConfigOptionList;
import fr.eyzox.forgecreeperheal.config.option.validator.BooleanValidator;
import fr.eyzox.forgecreeperheal.config.option.validator.IValidator;
import fr.eyzox.forgecreeperheal.config.option.validator.IntValidator;
import fr.eyzox.forgecreeperheal.exception.config.InvalidDataTypeException;

public class Config {

	private final Map<String, ConfigOptionGroup> groups = new LinkedHashMap<String, ConfigOptionGroup>();
	
	public Config() {
		
	}
	
	public void addOptionGroup(final ConfigOptionGroup optionGroup) {
		this.groups.put(optionGroup.getName(), optionGroup);
	}
	
	public void removeOptionGroup(final ConfigOptionGroup optionGroup) {
		this.removeOptionGroup(optionGroup.getName());
	}
	
	public void removeOptionGroup(final String optionGroupeName) {
		this.groups.remove(optionGroupeName);
	}
	
	public ConfigOptionGroup getOptionGroup(final String name) {
		return groups.get(name);
	}
	
	public Collection<ConfigOptionGroup> getOptionGroups() {
		return groups.values();
	}
	
	
	public static Config loadDefaultConfig() {
		
		final IntValidator positiveIntValidator = new IntValidator(0, Integer.MAX_VALUE);
		
		//## ConfigOptions
		
		final ConfigOption minTickBeforeStartHeal = new ConfigOption("minTickBeforeStartHeal");
		final ConfigOption maxTickBeforeStartHeal = new ConfigOption("maxTickBeforeStartHeal");
		final ConfigOption minTickBetweenEachHeal = new ConfigOption("minTickBetweenEachHeal");
		final ConfigOption maxTickBetweenEachHeal = new ConfigOption("maxTickBetweenEachHeal");
		
		final ConfigOption overrideBlock = new ConfigOption("overrideBlock");
		final ConfigOption overrideFluid = new ConfigOption("overrideFluid");
		final ConfigOption dropIfAlreadyBlock = new ConfigOption("dropIfAlreadyBlock");
		
		final ConfigOption dropItemsFromContainers = new ConfigOption("dropItemsFromContainers");
		
		final ConfigOptionList removeException = new ConfigOptionList("removeException");
		final ConfigOptionList healException = new ConfigOptionList("healException");
		final ConfigOptionList fromEntityException = new ConfigOptionList("fromEntityException");
		
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
		removeExceptionValues.add("minecraft:tnt");
		removeException.setValues(removeExceptionValues);
		final List<String> healExceptionValues = new ArrayList<String>(1);
		healExceptionValues.add("minecraft:tnt");
		healException.setValues(healExceptionValues);
		final List<String> fromEntityExceptionValues = new ArrayList<String>();
		fromEntityException.setValues(fromEntityExceptionValues);
		
		//## Validator
		
		minTickBeforeStartHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidDataTypeException {
				if(positiveIntValidator.isValid(value)) {
					 if(Integer.parseInt(value) <= Integer.parseInt(maxTickBeforeStartHeal.getValue())) {
						 return true;
					 }else {
						 throw new InvalidDataTypeException(String.format("INTEGER <= %s [%s]", maxTickBeforeStartHeal.getName(), maxTickBeforeStartHeal.getValue()), value);
					 }
				}else {
					return false;
				}
			}
		});
		
		maxTickBeforeStartHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidDataTypeException {
				if(positiveIntValidator.isValid(value)) {
					 if(Integer.parseInt(minTickBeforeStartHeal.getValue()) <= Integer.parseInt(value)) {
						 return true;
					 }else {
						 throw new InvalidDataTypeException(String.format("INTEGER >= %s [%s]", minTickBeforeStartHeal.getName(), minTickBeforeStartHeal.getValue()), value);
					 }
				}else {
					return false;
				}
			}
		});
		
		minTickBetweenEachHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidDataTypeException {
				if(positiveIntValidator.isValid(value)) {
					 if(Integer.parseInt(value) <= Integer.parseInt(maxTickBetweenEachHeal.getValue())) {
						 return true;
					 }else {
						 throw new InvalidDataTypeException(String.format("INTEGER <= %s [%s]", maxTickBetweenEachHeal.getName(), maxTickBetweenEachHeal.getValue()), value);
					 }
				}else {
					return false;
				}
			}
		});
		
		maxTickBetweenEachHeal.setValidator(new IValidator() {
			@Override
			public boolean isValid(String value) throws InvalidDataTypeException {
				if(positiveIntValidator.isValid(value)) {
					 if(Integer.parseInt(minTickBetweenEachHeal.getValue()) <= Integer.parseInt(value)) {
						 return true;
					 }else {
						 throw new InvalidDataTypeException(String.format("INTEGER >= %s [%s]", minTickBetweenEachHeal.getName(), minTickBetweenEachHeal.getValue()), value);
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
		dropIfAlreadyBlock.setDescription("if "+overrideBlock.getName()+" then the replacement block is dropped, else the block about to be heal is dropped");
		
		dropItemsFromContainers.setDescription("Drops all items from a container when it blows up, else items are restored with the container when healed (Only affect new explosions)");
		
		removeException.setDescription("These blocks will be not removed after a handled explosion and will be affected by the explosion");
		healException.setDescription("These blocks will be not healed");
		fromEntityException.setDescription("Forge Creeper Heal does not handle explosion triggered by entity from these entities");
		
		//## ConfigOptionGroups
		
		final ConfigOptionGroup healingTimeGroup = new ConfigOptionGroup("healing time");
		healingTimeGroup.addOption(minTickBeforeStartHeal);
		healingTimeGroup.addOption(maxTickBeforeStartHeal);
		healingTimeGroup.addOption(minTickBetweenEachHeal);
		healingTimeGroup.addOption(maxTickBetweenEachHeal);
		
		final ConfigOptionGroup overrideGroup = new ConfigOptionGroup("override");
		overrideGroup.addOption(overrideBlock);
		overrideGroup.addOption(overrideFluid);
		overrideGroup.addOption(dropIfAlreadyBlock);
		
		final ConfigOptionGroup containerGroup = new ConfigOptionGroup("containers");
		containerGroup.addOption(dropItemsFromContainers);
		
		final ConfigOptionGroup filtersGroup =  new ConfigOptionGroup("filters");
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
