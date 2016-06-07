package fr.eyzox.forgecreeperheal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import fr.eyzox.forgecreeperheal.json.adapter.BlockAdapter;
import fr.eyzox.forgecreeperheal.json.adapter.ClassAdapter;

public class Config {
	private int minimumTicksBeforeHeal;
	private int randomTickVar;
	private boolean override;
	private boolean overrideFluid;
	private boolean dropItemsFromContainer;
	private boolean dropIfAlreadyBlock;
	

	private Set<Block> removeException = new HashSet<Block>();
	private Set<Block> healException = new HashSet<Block>();
	private Set<Class<? extends Entity>> fromEntityException = new HashSet<Class<? extends Entity>>();
	
	private transient File configFile;
	
	public Config() {
		minimumTicksBeforeHeal = 6000;
		randomTickVar = 12000;
		override = false;
		overrideFluid = true;
		dropItemsFromContainer = true;
		dropIfAlreadyBlock = false;
		
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

	public File getConfigFile() {
		return configFile;
	}
	
	public void setConfigFile(File f) {
		this.configFile = f;
	}
	
	private void loadDefaultConfig() {
		removeException.add(Blocks.TNT);
		healException.add(Blocks.TNT);
	}
	
	public void save() {
		Gson gson = getGsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).create();
		try {
			PrintWriter pw = new PrintWriter(configFile);
			gson.toJson(this, pw);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			ForgeCreeperHeal.getLogger().warn("Unable to save configuration file at "+configFile.getAbsolutePath());
		}
	}
	
	public static Config loadConfig(File configFile) {
		System.out.println("load config");
		
		Config c = null;
		
		if(configFile.exists() && configFile.length() > 0) {
			Gson gson = getGsonBuilder().create();
			try {
				c = gson.fromJson(new FileReader(configFile), Config.class);
				c.setConfigFile(configFile);
			} catch (JsonParseException e) {
				e.printStackTrace();
				ForgeCreeperHeal.getLogger().warn("Unable to load configuration from "+configFile.getAbsolutePath()+" loading default configuration");
				c = new Config();
				c.loadDefaultConfig();
			} catch (FileNotFoundException e) {
				ForgeCreeperHeal.getLogger().info("Unable to find configuration file from "+configFile.getAbsolutePath()+" : creating a config file at this location with default configuration");
				c = createNewConfig(configFile);
			}
		}else {
			c = createNewConfig(configFile);
		}
		
		c.correctIllegalValues();
		
		return c;
	}
	
	private void correctIllegalValues() {
		if(minimumTicksBeforeHeal <= 0) minimumTicksBeforeHeal = 1;
		if(randomTickVar <= 0) randomTickVar = 1;
	}
	
	private static Config createNewConfig(File configFile) {
		Config c = new Config();
		c.loadDefaultConfig();
		c.setConfigFile(configFile);
		c.save();
		return c;
	}
	
	private static GsonBuilder getGsonBuilder() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeHierarchyAdapter(Block.class, new BlockAdapter());
		gson.registerTypeHierarchyAdapter(Class.class, new ClassAdapter());
		return gson;
	}

	@Override
	public String toString() {
		return "Config ["
				+ "\n\tminimumTicksBeforeHeal=" + minimumTicksBeforeHeal
				+ "\n\trandomTickVar=" + randomTickVar
				+ "\n\toverride=" + override
				+ "\n\tdropItempFromContainer=" + dropItemsFromContainer
				+ "\n\tremoveException=" + removeException
				+ "\n\thealException="+ healException
				+ "\n\tfromEntityException="+ fromEntityException
				+ "\n]";
	}
	
	
}
