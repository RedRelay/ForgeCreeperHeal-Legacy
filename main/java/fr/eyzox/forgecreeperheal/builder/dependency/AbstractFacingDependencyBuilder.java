package fr.eyzox.forgecreeperheal.builder.dependency;

import fr.eyzox.dependencygraph.DependencyType;
import fr.eyzox.dependencygraph.SingleDependency;
import fr.eyzox.dependencygraph.interfaces.IDependencyUpdater;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.builder.AbstractFactoryBuilder;
import fr.eyzox.forgecreeperheal.builder.dependency.property.IPropertySelector;
import fr.eyzox.forgecreeperheal.dependency.FullAndDependency;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractFacingDependencyBuilder extends AbstractFactoryBuilder implements IDependencyBuilder{

	private final IPropertySelector facingProperty;
	
	public AbstractFacingDependencyBuilder(Class<? extends Block> clazz, final IPropertySelector facingProperty) {
		super(clazz);
		this.facingProperty = facingProperty;
	}

	@Override
	public DependencyType<BlockPos, IBlockData> getDependencies(IBlockData data) {
		final EnumFacing facing = getEnumFacing(data);
		return new SingleDependency<BlockPos, IBlockData>(FacingDependencyUtils.getBlockPos(data.getPos(), facing));
	}
	
	public IPropertySelector getFacingProperty() {
		return facingProperty;
	}

	protected abstract EnumFacing getEnumFacing(final IBlockData data);

}
