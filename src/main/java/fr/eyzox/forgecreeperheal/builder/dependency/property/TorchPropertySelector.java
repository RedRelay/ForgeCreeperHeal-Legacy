package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.PropertyDirection;

public class TorchPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockTorch.FACING;
	}

}
