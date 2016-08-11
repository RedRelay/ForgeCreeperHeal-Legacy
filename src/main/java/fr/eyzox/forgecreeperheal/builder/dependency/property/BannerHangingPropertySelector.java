package fr.eyzox.forgecreeperheal.builder.dependency.property;

import net.minecraft.block.BlockBanner.BlockBannerHanging;
import net.minecraft.block.properties.PropertyDirection;

public class BannerHangingPropertySelector implements IPropertySelector {

	@Override
	public PropertyDirection getPropertyDirection() {
		return BlockBannerHanging.FACING;
	}

}
