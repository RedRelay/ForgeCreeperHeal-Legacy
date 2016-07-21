package fr.eyzox.forgecreeperheal.builder.blockdata;

import java.util.ArrayList;

import fr.eyzox.forgecreeperheal.blockdata.DefaultBlockData;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.blockdata.MultiBlockData;
import fr.eyzox.forgecreeperheal.blockdata.TileEntityBlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.IMultiSelector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class MultiBlockDataBuilder extends TileEntityBlockDataBuilder {

	private final IMultiSelector sel;

	public MultiBlockDataBuilder(Class<? extends Block> clazz, final IMultiSelector sel) {
		super(clazz);
		this.sel = sel;
	}

	@Override
	public IBlockData create(World w, BlockPos pos, IBlockState state, NBTTagCompound tileEntity) {

		final BlockPos[] otherPosArray = sel.getBlockPos(w,pos,state);

		final ArrayList<IBlockData> otherList = new ArrayList<IBlockData>(otherPosArray.length); 

		for(final BlockPos otherPos : otherPosArray) {
			final IBlockState otherState = w.getBlockState(otherPos);
			IBlockData other = null;
			if(otherState.getBlock().hasTileEntity(otherState)) {
				other = new TileEntityBlockData(otherPos, otherState , BlockDataBuilderUtils.getTileEntityNBT(w, otherPos, otherState));
			}else {
				other = new DefaultBlockData(otherPos, otherState);
			}
			otherList.add(other);
		}

		return new MultiBlockData(pos, state, tileEntity, otherList);

	}
	
	@Override
	public IBlockData create(NBTTagCompound tag) {
		return new MultiBlockData(tag);
	}
	
	public IMultiSelector getMultiSelector() {
		return sel;
	}

}
