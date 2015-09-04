package fr.eyzox.forgecreeperheal.healgraph.matcherfactories;

import fr.eyzox.forgecreeperheal.healgraph.BlockData;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public abstract class BlockClassMatcherFactory implements IBlockDataMatcherFactory {

	private Class<?> clazz;
	
	public BlockClassMatcherFactory(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public IBlockDataMatcher getMatcher(BlockData blockData) {
		if(clazz.isAssignableFrom(blockData.getBlockState().getBlock().getClass())) {
			return getMatcherForThisClass(blockData);
		}
		return null;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	protected abstract IBlockDataMatcher getMatcherForThisClass(BlockData blockData);

}
