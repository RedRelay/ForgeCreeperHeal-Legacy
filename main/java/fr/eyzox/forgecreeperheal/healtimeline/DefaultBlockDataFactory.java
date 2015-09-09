package fr.eyzox.forgecreeperheal.healtimeline;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class DefaultBlockDataFactory implements IBlockDataFactory {

	@Override
	public boolean accept(World world, BlockPos pos, IBlockState state) {
		return !(state.getBlock().isAir(world, pos) || ForgeCreeperHeal.getConfig().getHealException().contains(state.getBlock()));
	}
	
	@Override
	public BlockData createBlockData(World world, BlockPos pos, IBlockState state) {
		return new BlockData(state, getTileEntityToStore(world, pos, state));
	}
	
	protected TileEntity getTileEntityToStore(World world, BlockPos blockPos, IBlockState blockstate) {
		TileEntity tileEntity = world.getTileEntity(blockPos);
		if(tileEntity instanceof IInventory) {
			if(ForgeCreeperHeal.getConfig().isDropItemsFromContainer()) {
				NBTTagCompound buf = new NBTTagCompound();
				tileEntity.writeToNBT(buf);
				tileEntity = blockstate.getBlock().createTileEntity(world, blockstate);
				tileEntity.readFromNBT(buf);
			}
			((IInventory)tileEntity).clear();
		}
		return tileEntity;
	}

}
