package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.properties.PropertyDirection;

public class TripWireHookPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockTripWireHook.FACING;
	}

}
