package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.properties.PropertyDirection;

public class TrapDoorPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockTrapDoor.FACING;
	}

}
