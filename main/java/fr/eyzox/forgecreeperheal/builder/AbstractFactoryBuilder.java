package fr.eyzox.forgecreeperheal.builder;

import fr.eyzox.forgecreeperheal.factory.IData;
import net.minecraft.block.Block;

public abstract class AbstractFactoryBuilder implements IData<Block> {

	private final Class<? extends Block> clazz;
	
	public AbstractFactoryBuilder(final Class<? extends Block> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public boolean accept(Block block) {
		return this.clazz.isAssignableFrom(block.getClass());
	}

}
