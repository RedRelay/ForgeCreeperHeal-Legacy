package fr.eyzox.forgecreeperheal.healtimeline.factories;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;

public abstract class PropertyRequirementFactory extends ClassRequirementFactory {

	private IProperty property;
	
	public PropertyRequirementFactory(IProperty property) {
		this(Object.class, property);
	}
	
	public PropertyRequirementFactory(Class<?> clazz, IProperty property) {
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
	public BlockPos[] getRequiredBlockPos(BlockData blockData) {
		Enum e = (Enum)blockData.getBlockState().getValue(getProperty());
		BlockPos[] requiredBlockPos = getRequiredBlockPos(blockData, e);
		if(requiredBlockPos == null) {
			ForgeCreeperHeal.getLogger().warn("Unhandled Enum ["+e+"] in "+this.getClass().getCanonicalName());
		}
		return requiredBlockPos;
	}

	public abstract BlockPos[] getRequiredBlockPos(BlockData blockData, Enum e);

}
