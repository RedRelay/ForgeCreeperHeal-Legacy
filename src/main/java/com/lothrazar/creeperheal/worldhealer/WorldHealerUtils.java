package com.lothrazar.creeperheal.worldhealer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldHealerUtils {

  protected static void dropInventory(World world, BlockPos cp, IInventory inventory) {
    for (int inventoryIndex = 0; inventoryIndex < inventory.getSizeInventory(); ++inventoryIndex) {
      ItemStack itemstack = inventory.getStackInSlot(inventoryIndex);
      if (!itemstack.isEmpty()) {
        float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        for (float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.getCount() > 0;) {
          int j1 = world.rand.nextInt(21) + 10;
          if (j1 > itemstack.getCount()) {
            j1 = itemstack.getCount();
          }
          itemstack.shrink(j1);
          float f3 = 0.05F;
          ItemEntity entityitem = getEntityItem(world, cp, new ItemStack(itemstack.getItem(), j1), f, f1, f2, f3);
          if (entityitem != null) {
            if (itemstack.hasTag()) {
              entityitem.getItem().setTag(itemstack.getTag().copy());
            }
            world.addEntity(entityitem);
          }
        }
        /* ADDED TO REMOVE ITEMSTACK FROM INVENTORY */
        inventory.setInventorySlotContents(inventoryIndex, ItemStack.EMPTY);
      }
    }
  }

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
