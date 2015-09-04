package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import net.minecraft.block.BlockFalling;
import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.FacingMatchers;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public class FallingBlockMatcherFactory implements IBlockDataMatcherFactory {

	@Override
	public IBlockDataMatcher getMatcher(BlockData blockData) {
		if(blockData.getBlockState().getBlock() instanceof BlockFalling) {
			return FacingMatchers.PLACED_ABOVE;
		}
		return null;
	}

}
