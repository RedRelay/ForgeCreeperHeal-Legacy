package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableDoorBlock;

public class HealableDoorFactory extends AbstractHealableFactory{

	public HealableDoorFactory() {
		super(BlockDoor.class);
	}

	@Override
	public IHealable create(World world, BlockPos pos, IBlockState state) {
		if(state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
			return new HealableDoorBlock(world, pos, state);
		}
		return null;
	}

}
