package com.lothrazar.creeperheal.worldhealer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldHealerUtils {

  protected static ItemEntity getEntityItem(World world, BlockPos cp, ItemStack itemStack, float deltaX, float deltaY, float deltaZ, float motion) {
    if (itemStack.isEmpty() || itemStack.getItem() == null) {
      return null;
    }
    ItemEntity entityitem = new ItemEntity(world, cp.getX() + deltaX, cp.getY() + deltaY, cp.getZ() + deltaZ, itemStack);
    float motionX = (float) world.rand.nextGaussian() * motion;
    float motionY = (float) world.rand.nextGaussian() * motion + 0.2F;
    float motionZ = (float) world.rand.nextGaussian() * motion;
    entityitem.setMotion(motionX, motionY, motionZ);
    return entityitem;
  }
}
