package fr.eyzox.forgecreeperheal;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	/*
	 // old version looked like this
	 {
  "healing time": {
    "minTickStart": "100",
    "maxTickStart": "500",
    "minTick": "0",
    "maxTick": "200"
  },
 
 "override": {
    "overrideBlock": "false",
    "overrideFluid": "true",
    "dropIfCollision": "true"
  },

  "containers": {
    "dropItems": "false"
  },
  "filters": {
    "removeException": [
      "minecraft:tnt"
    ],
    "healException": [
      "minecraft:tnt"
    ],
    "sourceException": []
  }
}
	 * */
	private int minimumTicksBeforeHeal;
	private int randomTickVar;
	private boolean override;
	private boolean overrideFluid;
	private boolean dropItemsFromContainer;
	private boolean dropIfAlreadyBlock;
	private static Configuration forgeConfig;
	

	private Set<Block> removeException = new HashSet<Block>();
	private Set<Block> healException = new HashSet<Block>();
	private Set<Class<? extends Entity>> fromEntityException = new HashSet<Class<? extends Entity>>();
	
	public Config(Configuration conf) {
		forgeConfig = conf;
		//now set defaults
		minimumTicksBeforeHeal = 6000;
		randomTickVar = 12000;
		override = false;
		overrideFluid = true;
		dropItemsFromContainer = true;
		dropIfAlreadyBlock = false;
		removeException.add(Blocks.TNT);
		healException.add(Blocks.TNT);
		
		syncConfig();
	}
	 
	public void syncConfig(){
		forgeConfig.load();
		
		minimumTicksBeforeHeal = forgeConfig.getInt("minTickStart", "healing time", 
				6000, 0, 600000, "A lower number means it will heal faster");

		randomTickVar = forgeConfig.getInt("randomTickVar", "healing time", 
				12000, 0, 600000, "Determines the random nature of the heal");
		
		override = forgeConfig.getBoolean("overrideBlock", "override", false, "If the healing will replace blocks that were put in after");
		
		overrideFluid = forgeConfig.getBoolean("overrideFluid", "override", false, "If the healing will replace liquid (flowing or source) that were put in after");
		dropItemsFromContainer = forgeConfig.getBoolean("dropItemsFromContainer", "override", false, "");
		
		dropIfAlreadyBlock = forgeConfig.getBoolean("dropIfAlreadyBlock", "override", false, "If this is true, and a block tries to get healed but something is in the way, then that block will drop as an itemstack on the ground");
		

		boolean healTNT = forgeConfig.getBoolean("healTNT", "override", false, "If this is true, then TNT explosions will also heal as well as creepers");

		removeException = new HashSet<Block>();
		healException = new HashSet<Block>();
		if(healTNT){
			removeException.add(Blocks.TNT);
			healException.add(Blocks.TNT);
		}
		
		//TODO: the TNT exception list is not in config right now
		
		forgeConfig.save();
	}
	
	public int getMinimumTicksBeforeHeal() {
		return minimumTicksBeforeHeal;
	}
	public int getRandomTickVar() {
		return randomTickVar;
	}
	
	public boolean isOverride() {
		return override;
	}
	
	public boolean isOverrideFluid() {
		return overrideFluid;
	}

	public boolean isDropItemsFromContainer() {
		return dropItemsFromContainer;
	}

	public boolean isDropIfAlreadyBlock() {
		return dropIfAlreadyBlock;
	}

	public Set<Block> getRemoveException() {
		return removeException;
	}
	
	public Set<Block> getHealException() {
		return healException;
	}
	
	public Set<Class<? extends Entity>> getFromEntityException() {
		return fromEntityException;
	}


	public String toString(){
		return "minimumTicksBeforeHeal = "+minimumTicksBeforeHeal;
	}
}
