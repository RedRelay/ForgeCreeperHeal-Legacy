package com.lothrazar.creeperheal.worldhealer;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class WorldHealerUtils {

  protected static ItemEntity getEntityItem(Level world, BlockPos cp, ItemStack itemStack, float deltaX, float deltaY, float deltaZ, float motion) {
    if (itemStack.isEmpty() || itemStack.getItem() == null) {
      return null;
    }
    ItemEntity entityitem = new ItemEntity(world, cp.getX() + deltaX, cp.getY() + deltaY, cp.getZ() + deltaZ, itemStack);
    float motionX = (float) world.random.nextGaussian() * motion;
    float motionY = (float) world.random.nextGaussian() * motion + 0.2F;
    float motionZ = (float) world.random.nextGaussian() * motion;
    entityitem.setDeltaMovement(motionX, motionY, motionZ);
    return entityitem;
  }
}
