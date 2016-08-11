package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.PropertyDirection;

public class WallSignPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockWallSign.FACING;
	}

}
