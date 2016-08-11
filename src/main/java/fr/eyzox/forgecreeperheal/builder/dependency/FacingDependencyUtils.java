package fr.eyzox.forgecreeperheal.builder.dependency;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FacingDependencyUtils {

	private FacingDependencyUtils() {}
	
	public static BlockPos getBlockPos(final BlockPos pos, final EnumFacing facing) {
		return pos.offset(facing);
	}

}
