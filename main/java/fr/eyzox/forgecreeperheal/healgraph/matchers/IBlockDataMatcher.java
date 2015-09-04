package fr.eyzox.forgecreeperheal.healgraph.matchers;

import fr.eyzox.forgecreeperheal.healgraph.BlockData;

public interface IBlockDataMatcher {
	public boolean matches(BlockData node, BlockData data);
}
