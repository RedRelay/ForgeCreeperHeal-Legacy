package fr.eyzox.forgecreeperheal.builder.dependency;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FacingDependencyUtils {

	private FacingDependencyUtils() {}
	
	public static BlockPos getBlockPos(final BlockPos pos, final EnumFacing facing) {
		return pos.offset(facing);
	}

}
