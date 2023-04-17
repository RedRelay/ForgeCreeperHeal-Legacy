package com.lothrazar.creeperheal;

import com.lothrazar.library.config.ConfigTemplate;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigRegistryCreeperheal extends ConfigTemplate {

  private static ForgeConfigSpec CONFIG;
  private static IntValue MINTICKSBEFOREHEAL;
  private static IntValue RANDOMTICKVAR;
  private static BooleanValue OVERRIDEBLOCKS;
  //  private static BooleanValue OverrideFluids;
  private static BooleanValue DROPIFALREADYBLOCK;
  private static BooleanValue ONLYCREEPERS;
  static {
    final ForgeConfigSpec.Builder BUILDER = builder();
    BUILDER.push(ForgeCreeperHeal.MODID);
    //
    MINTICKSBEFOREHEAL = BUILDER.comment("A lower number means it will start healing faster").defineInRange("TickStartDelay", 600, 1, 600000);
    //
    RANDOMTICKVAR = BUILDER.comment("Determines the random nature of the heal.  Time between in ticks is the minimum + rand(1,this)").defineInRange("TickRandomInterval", 1200, 1, 600000);
    //    
    OVERRIDEBLOCKS = BUILDER.comment("If the healing will replace blocks that were put in after (such as fallen gravel or placed blocks)").define("OverrideBlocks", true);
    //    
    //    OverrideFluids = CFG.comment("If the healing will replace liquid that flowed into the exploded area").define("OverrideFluids", true);
    //     
    DROPIFALREADYBLOCK = BUILDER.comment("If this is true (and we are not overriding blocks), and a block tries to get healed but something is in the way,"
        + " then that block will drop as an itemstack on the ground")
        .define("DropBlockConflict", true);
    ONLYCREEPERS = BUILDER.comment("If this is true, only creeper explosions are healed.  Otherwise, all explosions will be healed (TNT, stuff from other mods, etc)")
        .define("OnlyCreepers", true);
    BUILDER.pop();
    CONFIG = BUILDER.build();
  }

  public ConfigRegistryCreeperheal() {
    CONFIG.setConfig(setup(ForgeCreeperHeal.MODID));
  }

  public static int getMinimumTicksBeforeHeal() {
    return MINTICKSBEFOREHEAL.get();
  }

  public static int getRandomTickVar() {
    return RANDOMTICKVAR.get();
  }

  public static boolean isOverride() {
    return OVERRIDEBLOCKS.get();
  }
  //  public boolean isOverrideFluid() {
  //    return OverrideFluids.get();
  //  }

  public static boolean isDropIfAlreadyBlock() {
    return DROPIFALREADYBLOCK.get();
  }

  public static boolean isOnlyCreepers() {
    return ONLYCREEPERS.get();
  }
}
