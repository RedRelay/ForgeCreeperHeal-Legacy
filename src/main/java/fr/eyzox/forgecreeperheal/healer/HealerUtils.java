package fr.eyzox.forgecreeperheal.healer;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HealerUtils {

	private HealerUtils() {
	}
	
	public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack) {
		spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
	
	private static final Random RANDOM = new Random();
	private static void spawnItemStack(World worldIn, double p_180173_1_, double p_180173_3_, double p_180173_5_, ItemStack p_180173_7_)
    {
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (p_180173_7_.stackSize > 0)
        {
            int i = RANDOM.nextInt(21) + 10;

            if (i > p_180173_7_.stackSize)
            {
                i = p_180173_7_.stackSize;
            }

            p_180173_7_.stackSize -= i;
            EntityItem entityitem = new EntityItem(worldIn, p_180173_1_ + (double)f, p_180173_3_ + (double)f1, p_180173_5_ + (double)f2, new ItemStack(p_180173_7_.getItem(), i, p_180173_7_.getMetadata()));

            if (p_180173_7_.hasTagCompound())
            {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)p_180173_7_.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * (double)f3;
            entityitem.motionY = RANDOM.nextGaussian() * (double)f3 + 0.20000000298023224D;
            entityitem.motionZ = RANDOM.nextGaussian() * (double)f3;
            worldIn.spawnEntityInWorld(entityitem);
        }
    }
}
