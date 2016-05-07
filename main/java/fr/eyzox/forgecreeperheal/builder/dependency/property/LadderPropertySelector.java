package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.PropertyDirection;

public class LadderPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockLadder.FACING;
	}

}
