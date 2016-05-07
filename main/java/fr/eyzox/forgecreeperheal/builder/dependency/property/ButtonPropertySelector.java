package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockButton;
import net.minecraft.block.properties.PropertyDirection;

public class ButtonPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockButton.FACING;
	}

}
