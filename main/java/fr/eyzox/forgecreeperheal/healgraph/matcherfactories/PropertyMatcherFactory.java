package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import net.minecraft.block.properties.IProperty;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public abstract class PropertyMatcherFactory extends BlockClassMatcherFactory {

	private IProperty property;
	
	public PropertyMatcherFactory(IProperty property) {
		this(Object.class, property);
	}
	
	public PropertyMatcherFactory(Class<?> clazz, IProperty property) {
		super(clazz);
		this.property = property;
	}

	public IProperty getProperty() {
		return property;
	}

	public void setProperty(IProperty property) {
		this.property = property;
	}

	@Override
	protected IBlockDataMatcher getMatcherForThisClass(BlockData blockData) {
		Enum e = (Enum)blockData.getBlockState().getValue(getProperty());
		IBlockDataMatcher m = getMatcher(blockData, e);
		if(m == null) {
			ForgeCreeperHeal.getLogger().warn("Unhandled Enum ["+e+"] in "+this.getClass().getCanonicalName());
		}
		return m;
	}
	
	protected abstract IBlockDataMatcher getMatcher(BlockData blockData, Enum e);

}
