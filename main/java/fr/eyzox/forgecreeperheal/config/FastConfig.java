package fr.eyzox.forgecreeperheal.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.eyzox.bsc.config.Config;
import fr.eyzox.bsc.config.ConfigOptionGroup;
import fr.eyzox.bsc.config.IConfigListener;
import fr.eyzox.bsc.config.option.ConfigOption;
import fr.eyzox.bsc.config.option.ConfigOptionList;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class FastConfig implements IConfigListener {

	private int minTickStart, maxTickStart;
	private int minTick, maxTick;
	
	private boolean overrideBlock, overrideFluid;
	private boolean dropIfCollision;
	
	private boolean dropItems;
	
	private final Set<Block> removeException = new HashSet<Block>();
	private final Set<Block> healException = new HashSet<Block>();
	private final Set<Class<? extends Entity>> sourceException = new HashSet<Class<? extends Entity>>();
	
	private final Set<Block> protectedRemoveException = Collections.unmodifiableSet(removeException);
	private final Set<Block> protectedHealException = Collections.unmodifiableSet(healException);
	private final Set<Class<? extends Entity>> protectedSourceException = Collections.unmodifiableSet(sourceException);
	
	public FastConfig() {}

	@Override
	public void onChange(Config config) {
		
		removeException.clear();
		healException.clear();
		sourceException.clear();
		
		ConfigOptionGroup group = config.getOptionGroup(ConfigProvider.GROUP_HEALING_TIME);
		
		this.minTickStart = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MIN_TICK_BEFORE_HEAL)).getValue());
		this.maxTickStart = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MAX_TICK_BEFORE_HEAL)).getValue());
		
		this.minTick = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MIN_TICK)).getValue());
		this.maxTick = Integer.parseInt(((ConfigOption)group.getOption(ConfigProvider.OPTION_MAX_TICK)).getValue());
		
		group = config.getOptionGroup(ConfigProvider.GROUP_OVERRIDE);
		
		this.overrideBlock = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_OVERRIDE_BLOCK)).getValue());
		this.overrideFluid = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_OVERRIDE_FUILD)).getValue());
		this.dropIfCollision = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_DROP_IF_COLLISION)).getValue());
		
		group = config.getOptionGroup(ConfigProvider.GROUP_CONTAINERS);
		
		this.dropItems = Boolean.parseBoolean(((ConfigOption)group.getOption(ConfigProvider.OPTION_DROP_ITEMS)).getValue());
		
		group = config.getOptionGroup(ConfigProvider.GROUP_FILTERS);
		
		this.fillBlockSet(removeException, group, ConfigProvider.OPTION_REMOVE_EXCEPTION);
		this.fillBlockSet(healException, group, ConfigProvider.OPTION_HEAL_EXCEPTION);
		
		Collection<String> allClazz = ((ConfigOptionList)group.getOption(ConfigProvider.OPTION_SOURCE_EXCEPTION)).getValues();
		for(final String clazzName : allClazz) {
			Class<? extends Entity> clazz = null;
			try {
				clazz = (Class<? extends Entity>) Class.forName(clazzName);
			} catch (ClassNotFoundException e) {
				ForgeCreeperHeal.getLogger().warn(group.getName()+" > "+ConfigProvider.OPTION_SOURCE_EXCEPTION+" > Unable to find class : "+clazzName);
			} catch (ClassCastException e2) {
				ForgeCreeperHeal.getLogger().error(group.getName()+" > "+ConfigProvider.OPTION_SOURCE_EXCEPTION+" > class : \""+clazzName+"\" must be an Entity");
			}
			
			if(clazz != null) {
				sourceException.add(clazz);
			}
		}
		

	}
	
	private void fillBlockSet(final Set<Block> set, final ConfigOptionGroup group, final String optionName) {
		final Collection<String> allBlockNames = ((ConfigOptionList)group.getOption(optionName)).getValues();
		for(final String blockName : allBlockNames) {
			final Block block = Block.getBlockFromName(blockName);
			if(block != null) {
				set.add(block);
			}else {
				ForgeCreeperHeal.getLogger().warn(group.getName()+" > "+optionName+" > Unable to find block : "+blockName);
			}
		}
	}

	public int getMinTickStart() {
		return minTickStart;
	}

	public int getMaxTickStart() {
		return maxTickStart;
	}

	public int getMinTick() {
		return minTick;
	}

	public int getMaxTick() {
		return maxTick;
	}

	public boolean isOverrideBlock() {
		return overrideBlock;
	}

	public boolean isOverrideFluid() {
		return overrideFluid;
	}

	public boolean isDropIfCollision() {
		return dropIfCollision;
	}

	public boolean isDropItems() {
		return dropItems;
	}

	public Set<Block> getRemoveException() {
		return protectedRemoveException;
	}

	public Set<Block> getHealException() {
		return protectedHealException;
	}

	public Set<Class<? extends Entity>> getSourceException() {
		return protectedSourceException;
	}
	
	

}
