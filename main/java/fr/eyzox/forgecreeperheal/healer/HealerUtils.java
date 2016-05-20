package fr.eyzox.forgecreeperheal.healer;

import java.lang.reflect.Field;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.reflection.Reflect;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fluids.FluidRegistry;

public class HealerUtils {
	
	private static final Field extendedBlockStorageField = Reflect.getFieldForClass(Chunk.class, "storageArrays", "field_76652_q");
	
	public static void dropInventory(World world, BlockPos cp, IInventory inventory) {
		for (int inventoryIndex = 0; inventoryIndex < inventory.getSizeInventory(); ++inventoryIndex)
		{
			ItemStack itemstack = inventory.getStackInSlot(inventoryIndex);

			if (itemstack != null)
			{

				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;

				for (float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; )
				{
					int j1 = world.rand.nextInt(21) + 10;

					if (j1 > itemstack.stackSize)
					{
						j1 = itemstack.stackSize;
					}

					itemstack.stackSize -= j1;

					float f3 = 0.05F;
					EntityItem entityitem = getEntityItem(world, cp, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()), f, f1, f2, f3);
					


					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
					
					world.spawnEntityInWorld(entityitem);
				}

				//ADDED TO REMOVE ITEMSTACK FROM INVENTORY
				inventory.setInventorySlotContents(inventoryIndex, null);
			}
		}
	}
	
	public static EntityItem getEntityItem(World world, BlockPos cp, ItemStack itemStack, float deltaX, float deltaY, float deltaZ, float motion) {
		EntityItem entityitem = new EntityItem(world, (double)((float)cp.getX() + deltaX), (double)((float)cp.getY() + deltaY), (double)((float)cp.getZ() + deltaZ), itemStack);
		entityitem.motionX = (double)((float)world.rand.nextGaussian() * motion);
		entityitem.motionY = (double)((float)world.rand.nextGaussian() * motion + 0.2F);
		entityitem.motionZ = (double)((float)world.rand.nextGaussian() * motion);
		return entityitem;
	}
	
	public static ExtendedBlockStorage[] getExtendedBlockStorageArray(Chunk c) {
		return (ExtendedBlockStorage[]) Reflect.getDataFromField(extendedBlockStorageField, c);
	}
	
	public static void healBlock(World world, BlockPos pos, IBlockState state, NBTTagCompound tileEntityTag, int flags) {
		IBlockState currentBlockState = world.getBlockState(pos);
		boolean isAir = currentBlockState.getBlock().isAir(currentBlockState, world, pos);

		if(ForgeCreeperHeal.getConfig().isOverrideBlock() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(currentBlockState.getBlock()) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfCollision() && !isAir) {
				world.spawnEntityInWorld(HealerUtils.getEntityItem(world, pos, new ItemStack(currentBlockState.getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));

				TileEntity te = world.getTileEntity(pos);
				if(te instanceof IInventory) {
					HealerUtils.dropInventory(world, pos, (IInventory) te);
				}
			}

			world.setBlockState(pos, state, flags);

			if(tileEntityTag != null) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					te.readFromNBT(tileEntityTag);
					world.setTileEntity(pos, te);
				}
			}

		}else if(ForgeCreeperHeal.getConfig().isDropIfCollision()){
			world.spawnEntityInWorld(HealerUtils.getEntityItem(world, pos, new ItemStack(state.getBlock()),world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));
			if(tileEntityTag != null) {
				TileEntity te = state.getBlock().createTileEntity(world, state);
				if(te instanceof IInventory) {
					te.readFromNBT(tileEntityTag);
					HealerUtils.dropInventory(world, pos, (IInventory) te);
				}
			}

		}
	}
}
