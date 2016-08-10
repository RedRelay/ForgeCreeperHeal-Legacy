package fr.eyzox.forgecreeperheal.healer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class WorldHealer {

	private final World world;
	private final Map<BlockPos, IBlockState> toUpdate = new LinkedHashMap<BlockPos, IBlockState>();
	
	public WorldHealer(final World w) {
		this.world = w;
	}
	
	public void heal(BlockPos pos, IBlockState newState, NBTTagCompound tileEntity) {
		final IBlockState currentBlockState = world.getBlockState(pos);
		final boolean isAir = currentBlockState.getBlock().isAir(currentBlockState, world, pos);

		//If current block = air or overriding
		if(ForgeCreeperHeal.getConfig().isOverrideBlock() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(currentBlockState.getBlock()) != null)) {
			
			//IF overriding a block : Drop current block & if this block's TileEntity is an IInventory, drop all Items contained
			if(ForgeCreeperHeal.getConfig().isDropIfCollision() && !isAir) {
				HealerUtils.spawnItemStack(world, pos, new ItemStack(currentBlockState.getBlock()));
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof IInventory) {
					InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
				}
			}

			//Restore Block, then its TileEntity
			world.setBlockState(pos, newState, 0);
			this.markForUpdate(pos, currentBlockState);
			if(tileEntity != null) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					te.readFromNBT(tileEntity);
					world.setTileEntity(pos, te);
				}else {
					ForgeCreeperHeal.getLogger().warn(String.format("Unable to restore a TileEntity \"%s\" because restored blockstate \"%s\", doesn't have TileEntity", tileEntity, newState));
				}
			}

		}
		// (( If current block != air and no overriding )) & drop if collision
		else if(ForgeCreeperHeal.getConfig().isDropIfCollision()){
			//Drop the block which should have been restored & if its TileEntity = IInventory, drop items contained
			HealerUtils.spawnItemStack(world, pos, new ItemStack(newState.getBlock()));
			if(tileEntity != null) {
				TileEntity te = newState.getBlock().createTileEntity(world, newState);
				if(te instanceof IInventory) {
					te.readFromNBT(tileEntity);
					InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
				}
			}

		}
	}
	
	public void markForUpdate(BlockPos pos, IBlockState oldState) {
		toUpdate.put(pos, oldState);
	}
	
	public void update(int flags) {
		if(toUpdate.isEmpty()) return;
		
		for(Entry<BlockPos, IBlockState> entry : toUpdate.entrySet()) {
			final BlockPos pos = entry.getKey();
			world.markAndNotifyBlock(pos,world.getChunkFromBlockCoords(pos), entry.getValue(), world.getBlockState(pos), flags);
		}
		
		toUpdate.clear();
	}
	
	public World getWorld() {
		return world;
	}

}
