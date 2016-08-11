package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.BedMultiSelector;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BedBlockDataBuilder extends MultiBlockDataBuilder {

	public BedBlockDataBuilder() {
		super(BlockBed.class, new BedMultiSelector());
	}
	
	@Override
	public BlockData create(World w, BlockPos pos, IBlockState state) {
		if(state.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
			return super.create(w, pos, state);
		}
		return null;
	}

}
