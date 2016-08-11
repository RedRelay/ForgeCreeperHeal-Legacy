package fr.eyzox.forgecreeperheal.blockdata.multi.selector;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class FacingMultiSelector implements IMultiSelector {

	public static final IMultiSelector FACING_UP = new FacingMultiSelector(EnumFacing.UP); 
	
	private final EnumFacing facing;
	
	public FacingMultiSelector(final EnumFacing facing) {
		this.facing = facing;
	}
	
	@Override
	public BlockPos[] getBlockPos(World w, BlockPos pos, IBlockState state) {
		return new BlockPos[]{pos.offset(facing)};
	}

}
