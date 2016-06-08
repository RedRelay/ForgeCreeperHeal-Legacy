package fr.eyzox.forgecreeperheal;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private int minimumTicksBeforeHeal;
	private int randomTickVar;
	private boolean overrideBlock;
	private boolean overrideFluid;

	private boolean dropIfAlreadyBlock;
	private boolean onlyCreepers;
	private static Configuration forgeConfig;
	
	public Config(Configuration conf) {
		forgeConfig = conf;
		//now set defaults
		minimumTicksBeforeHeal = 6000;
		randomTickVar = 12000;
		overrideBlock = false;
		overrideFluid = true; 
		dropIfAlreadyBlock = false;
		 
	
		syncConfig();
	}
	 
	public void syncConfig(){
		forgeConfig.load();
		
		String category = ForgeCreeperHeal.MODID;
		
		minimumTicksBeforeHeal = forgeConfig.getInt("Tick Start Delay", category, 
				600, 1, 600000, "A lower number means it will start healing faster");

		randomTickVar = forgeConfig.getInt("Tick Random Interval", category, 
				1200, 1, 600000, "Determines the random nature of the heal.  Time between in ticks is the minimum + rand(1,this)");
		
		overrideBlock = forgeConfig.getBoolean("Override Blocks", category, false, "If the healing will replace blocks that were put in after (such as fallen gravel or placed blocks)");
		
		overrideFluid = forgeConfig.getBoolean("Override Fluids", category, true, "If the healing will replace liquid that flowed into the exploded area");
		 
		dropIfAlreadyBlock = forgeConfig.getBoolean("Drop Block Conflict", category, true, "If this is true (and we are not overriding blocks), and a block tries to get healed but something is in the way, then that block will drop as an itemstack on the ground");
	
		onlyCreepers = forgeConfig.getBoolean("Only Creepers", category, true, "If this is true, only creeper explosions are healed.  Otherwise, all explosions will be healed (TNT, stuff from other mods, etc)");
		
		forgeConfig.save();
	}
	
	public int getMinimumTicksBeforeHeal() {
		return minimumTicksBeforeHeal;
	}
	public int getRandomTickVar() {
		return randomTickVar;
	}
	
	public boolean isOverride() {
		return overrideBlock;
	}
	
	public boolean isOverrideFluid() {
		return overrideFluid;
	}
 
	public boolean isDropIfAlreadyBlock() {
		return dropIfAlreadyBlock;
	}

	public String toString(){
		return "minimumTicksBeforeHeal = "+minimumTicksBeforeHeal;
	}

	public boolean isOnlyCreepers() {
		return onlyCreepers;
	}
    public Configuration getForgeConfig() {
    	return forgeConfig;
    }
}
