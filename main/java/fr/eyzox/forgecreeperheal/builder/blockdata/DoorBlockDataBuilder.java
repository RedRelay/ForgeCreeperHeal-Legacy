package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.blockdata.multi.selector.FacingMultiSelector;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class DoorBlockDataBuilder extends MultiBlockDataBuilder{

	public DoorBlockDataBuilder() {
		super(BlockDoor.class, FacingMultiSelector.FACING_UP);
	}

	@Override
	public BlockData create(World w, BlockPos pos, IBlockState state) {
		if(state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
			return super.create(w, pos, state);
		}
		return null;
	}

}
