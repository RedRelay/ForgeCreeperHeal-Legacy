package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public interface IBlockDataMatcherFactory {
	public IBlockDataMatcher getMatcher(BlockData blockData);
}
