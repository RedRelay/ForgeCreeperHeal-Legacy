package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.FacingMatchers;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public class FacingMatcherFactory extends PropertyMatcherFactory {

	public FacingMatcherFactory(IProperty property) {
		super(property);
	}

	public FacingMatcherFactory(Class<?> clazz, IProperty property) {
		super(clazz, property);
	}

	@Override
	protected IBlockDataMatcher getMatcher(BlockData blockData, Enum e) {
		switch((EnumFacing) e) {
		case EAST: return FacingMatchers.PLACED_EAST;
		case NORTH: return FacingMatchers.PLACED_NORTH;
		case SOUTH: return FacingMatchers.PLACED_SOUTH;
		case UP: return FacingMatchers.PLACED_ABOVE;
		case WEST: return FacingMatchers.PLACED_WEST;
		case DOWN: return FacingMatchers.PLACED_BELOW;
		default:
			return null;
		}
	}


}
