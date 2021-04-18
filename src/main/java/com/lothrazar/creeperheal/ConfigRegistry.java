package com.lothrazar.creeperheal;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.nio.file.Path;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigRegistry {

  private static final ForgeConfigSpec.Builder CFG = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  private static IntValue MinTicksBeforeHeal;
  private static IntValue RandomTickVar;
  private static BooleanValue OverrideBlocks;
  private static BooleanValue OverrideFluids;
  private static BooleanValue dropIfAlreadyBlock;
  private static BooleanValue onlyCreepers;
  static {
    initConfig();
  }

  private static void initConfig() {
    CFG.push(ForgeCreeperHeal.MODID);
    //
    MinTicksBeforeHeal = CFG.comment("A lower number means it will start healing faster").defineInRange("TickStartDelay", 600, 1, 600000);
    //
    RandomTickVar = CFG.comment("Determines the random nature of the heal.  Time between in ticks is the minimum + rand(1,this)").defineInRange("TickRandomInterval", 1200, 1, 600000);
    //    
    OverrideBlocks = CFG.comment("If the healing will replace blocks that were put in after (such as fallen gravel or placed blocks)").define("OverrideBlocks", true);
    //    
    OverrideFluids = CFG.comment("If the healing will replace liquid that flowed into the exploded area").define("OverrideFluids", true);
    //     
    dropIfAlreadyBlock = CFG.comment("If this is true (and we are not overriding blocks), and a block tries to get healed but something is in the way, then that block will drop as an itemstack on the ground")
        .define("DropBlockConflict", true);
    onlyCreepers = CFG.comment("If this is true, only creeper explosions are healed.  Otherwise, all explosions will be healed (TNT, stuff from other mods, etc)")
        .define("OnlyCreepers", true);
    CFG.pop();
    COMMON_CONFIG = CFG.build();
  }

  public static void setup(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }

  public static int getMinimumTicksBeforeHeal() {
    return MinTicksBeforeHeal.get();
  }

  public static int getRandomTickVar() {
    return RandomTickVar.get();
  }

  public static boolean isOverride() {
    return OverrideBlocks.get();
  }
  //  public boolean isOverrideFluid() {
  //    return OverrideFluids.get();
  //  }

  public static boolean isDropIfAlreadyBlock() {
    return dropIfAlreadyBlock.get();
  }

  public static boolean isOnlyCreepers() {
    return onlyCreepers.get();
  }
}
