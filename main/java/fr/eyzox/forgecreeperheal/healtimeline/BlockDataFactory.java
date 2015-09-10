package fr.eyzox.forgecreeperheal.healtimeline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class BlockDataFactory implements IBlockDataFactory {
	
	private static final BlockDataFactory INSTANCE = new BlockDataFactory();
	
	private final List<IBlockDataFactory> blockDataFactories = new LinkedList<IBlockDataFactory>();
	private final IBlockDataFactory defaultBlockDataFactory = new DefaultBlockDataFactory();
	
	private BlockDataFactory(){
		init();
	}
	
	private void init() {
		
	}

	@Override
	public boolean accept(World world, BlockPos pos, IBlockState state) {
		return defaultBlockDataFactory.accept(world, pos, state);
	}

	@Override
	public BlockData createBlockData(World world, BlockPos pos, IBlockState state) {
		Iterator<IBlockDataFactory> it = blockDataFactories.iterator();
		BlockData data = null;
		while(data == null && it.hasNext()) {
			IBlockDataFactory factory = it.next();
			if(factory.accept(world, pos, state)) {
				data = factory.createBlockData(world, pos, state);
			}
		}
		
		if(data == null && defaultBlockDataFactory.accept(world, pos, state)) {
			data = defaultBlockDataFactory.createBlockData(world, pos, state);
		}
		
		return data;
	}
	
	private static class DefaultBlockDataFactory implements IBlockDataFactory {

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
	
	
	public static IBlockDataFactory getInstance() {
		return INSTANCE;
	}
	

}
