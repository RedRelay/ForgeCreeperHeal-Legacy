package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.builder.dependency.property.IPropertySelector;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;

public class OppositeFacingDependencyBuilder extends FacingDependencyBuilder{

	public OppositeFacingDependencyBuilder(Class<? extends Block> clazz, final IPropertySelector facingProperty) {
		super(clazz, facingProperty);
	}

	@Override
	protected EnumFacing getEnumFacing(IBlockData data) {
		return super.getEnumFacing(data).getOpposite();
	}

}
