package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.properties.PropertyDirection;

public class CocoaPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockCocoa.FACING;
	}

}
