package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.BedMultiSelector;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BedBlockDataBuilder extends MultiBlockDataBuilder {

	public BedBlockDataBuilder() {
		super(BlockBed.class, new BedMultiSelector());
	}
	
	@Override
	public IBlockData create(World w, BlockPos pos, IBlockState state, NBTTagCompound tileEntity) {
		if(state.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
			return super.create(w, pos, state, tileEntity);
		}
		return null;
	}

}
