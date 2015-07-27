package fr.eyzox.forgecreeperheal.handler;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ExplosionEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.BlockData;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealerUtils;

public class ExplosionEventHandler {

	@SubscribeEvent
	public void onDetonate(ExplosionEvent.Detonate event) {
		if(!event.world.isRemote && event.explosion.exploder != null && !ForgeCreeperHeal.getConfig().getFromEntityException().contains(event.explosion.exploder.getClass())) {
			WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.world);
			if(worldHealer != null) {
				worldHealer.onDetonate(event);
			}
		}
	}

}
