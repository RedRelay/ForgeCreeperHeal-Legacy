package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.FacingMatchers;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public class SupportByBottomMatcherFactory extends BlockClassMatcherFactory {

	public SupportByBottomMatcherFactory(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected IBlockDataMatcher getMatcherForThisClass(BlockData blockData) {
		return FacingMatchers.PLACED_ABOVE;
	}

}
