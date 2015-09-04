package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.FacingMatchers;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public class LeverMatcherFactory extends PropertyMatcherFactory {

	public LeverMatcherFactory() {
		super(BlockLever.class, BlockLever.FACING);
	}

	@Override
	protected IBlockDataMatcher getMatcher(BlockData blockData, Enum e) {
		switch((EnumOrientation)e) {
		case EAST: return FacingMatchers.PLACED_EAST;
		case NORTH: return FacingMatchers.PLACED_NORTH;
		case SOUTH: return FacingMatchers.PLACED_SOUTH;
		case WEST: return FacingMatchers.PLACED_WEST;
		default:
			if(e == EnumOrientation.DOWN_X || e == EnumOrientation.DOWN_Z) {
				return FacingMatchers.PLACED_ABOVE;
			}else if(e == EnumOrientation.UP_X || e == EnumOrientation.UP_Z) {
				return FacingMatchers.PLACED_BELOW;
			}
			return null;
		}
	}


}
