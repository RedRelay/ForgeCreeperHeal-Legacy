package fr.eyzox.forgecreeperheal.builder.blockdata;

import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.blockdata.DefaultBlockData;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.blockdata.MultiBlockData;
import fr.eyzox.forgecreeperheal.blockdata.TileEntityBlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.IMultiSelector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
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

		final List<IBlockData> otherList = new LinkedList<IBlockData>(); 

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

		final IBlockData[] others = new IBlockData[otherList.size()];
		otherList.toArray(others);

		return new MultiBlockData(pos, state, tileEntity, others);

	}
	
	@Override
	public IBlockData create(NBTTagCompound tag) {
		return new MultiBlockData(tag);
	}
	
	public IMultiSelector getMultiSelector() {
		return sel;
	}

}
