package fr.eyzox.forgecreeperheal.builder.blockdata;

import java.util.ArrayList;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.blockdata.MultiBlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.IMultiSelector;
import fr.eyzox.forgecreeperheal.builder.AbstractFactoryBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockDataBuilder extends AbstractFactoryBuilder implements IBlockDataBuilder {

	private final IMultiSelector sel;

	public MultiBlockDataBuilder(Class<? extends Block> clazz, final IMultiSelector sel) {
		super(clazz);
		this.sel = sel;
	}

	@Override
	public BlockData create(World w, BlockPos pos, IBlockState state) {

		final BlockPos[] otherPosArray = sel.getBlockPos(w,pos,state);

		final ArrayList<BlockData> otherList = new ArrayList<BlockData>(otherPosArray.length); 

		for(final BlockPos otherPos : otherPosArray) {
			final IBlockState otherState = w.getBlockState(otherPos);
			BlockData other = new BlockData(otherPos, otherState);
			//TODO Rework MultiBlockData : make it recursive
			if(otherState.getBlock().hasTileEntity(otherState)) {
				TileEntity te = w.getTileEntity(otherPos);
				if(te != null) {
					other.processTileEntity(te);
				}
			}
			otherList.add(other);
		}

		return new MultiBlockData(pos, state, otherList);

	}
	
	@Override
	public BlockData create(NBTTagCompound tag) {
		return new MultiBlockData(tag);
	}
	
	public IMultiSelector getMultiSelector() {
		return sel;
	}

}
