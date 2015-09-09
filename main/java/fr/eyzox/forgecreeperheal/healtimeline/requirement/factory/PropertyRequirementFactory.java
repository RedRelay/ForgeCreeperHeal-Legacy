package fr.eyzox.forgecreeperheal.healtimeline.requirement.factory;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.BlockData;
import fr.eyzox.timeline.Key;

public abstract class PropertyRequirementFactory extends BlockClassRequirementFactory {

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
	public BlockPos[] getKeyDependencies(Key<BlockPos,BlockData> blockData) {
		Enum e = (Enum)blockData.getValue().getBlockState().getValue(getProperty());
		BlockPos[] requiredBlockPos = getRequiredBlockPos(blockData, e);
		if(requiredBlockPos == null) {
			ForgeCreeperHeal.getLogger().warn("Unhandled Enum ["+e+"] in "+this.getClass().getCanonicalName());
		}
		return requiredBlockPos;
	}

	public abstract BlockPos[] getRequiredBlockPos(Key<BlockPos,BlockData> blockData, Enum e);

}
