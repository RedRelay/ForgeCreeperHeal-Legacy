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

public class ExplosionEventHandler {

	private Random rdn = new Random();

	@SubscribeEvent
	public void onDetonate(ExplosionEvent.Detonate event) {
		if(!event.world.isRemote && !ForgeCreeperHeal.getConfig().getFromEntityException().contains(event.explosion.exploder.getClass())) {
			WorldHealer worldHealer = ForgeCreeperHeal.getWorldHealer((WorldServer) event.world);
			if(worldHealer != null) {
				int maxTicksBeforeHeal = 0;
				//Process primary blocks
				for(ChunkPosition c : event.getAffectedBlocks()) {
					Block block = event.world.getBlock(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
					if(block.isNormalCube()) {

						int ticksBeforeHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal()+rdn.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar());
						if(ticksBeforeHeal > maxTicksBeforeHeal) {
							maxTicksBeforeHeal = ticksBeforeHeal;
						}



						processBlock(worldHealer, event.world, c, block, ticksBeforeHeal);



					}
				}
				maxTicksBeforeHeal++;

				//Process secondary blocks
				for(ChunkPosition c : event.getAffectedBlocks()) {
					Block block = event.world.getBlock(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
					if(!block.isNormalCube() && !block.isAir(event.world, c.chunkPosX, c.chunkPosY, c.chunkPosZ)) {
						processBlock(worldHealer, event.world, c, block, maxTicksBeforeHeal + rdn.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar()));
					}
				}
			}
		}
	}

	private void processBlock(WorldHealer worldHealer, World world, ChunkPosition c, Block block, int ticks) {
		if(ForgeCreeperHeal.getConfig().isDropItemsFromContainer() && !ForgeCreeperHeal.getConfig().getRemoveException().contains(block)) {
			TileEntity te = world.getTileEntity(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
			if(te instanceof IInventory) {
				worldHealer.dropInventory((IInventory) te, c);
			}
		}

		if(!ForgeCreeperHeal.getConfig().getHealException().contains(block)) {
			worldHealer.add(new BlockData(world,c), ticks);
		}

		if(!ForgeCreeperHeal.getConfig().getRemoveException().contains(block)) {
			world.removeTileEntity(c.chunkPosX, c.chunkPosY, c.chunkPosZ);
			world.setBlock(c.chunkPosX, c.chunkPosY, c.chunkPosZ, Blocks.air, 0, 7);
		}
	}

}
