package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.forgecreeperheal.blockdata.BlockData;
import fr.eyzox.forgecreeperheal.builder.dependency.property.IPropertySelector;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;

public class FacingDependencyBuilder extends AbstractFacingDependencyBuilder{

	public FacingDependencyBuilder(Class<? extends Block> clazz, final IPropertySelector facingProperty) {
		super(clazz, facingProperty);
	}

	@Override
	protected EnumFacing getEnumFacing(BlockData data) {
		return (EnumFacing) data.getState().getValue(getFacingProperty().getPropertyDirection());
	}

}
