package fr.eyzox.forgecreeperheal.healer;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HealerUtils {

	private HealerUtils() {
	}
	
	public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack) {
		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
}
