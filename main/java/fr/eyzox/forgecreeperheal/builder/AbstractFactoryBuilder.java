package fr.eyzox.forgecreeperheal.builder;

import fr.eyzox.forgecreeperheal.factory.IData;
import net.minecraft.block.Block;

public abstract class AbstractFactoryBuilder implements IData<Class<? extends Block>> {

	private final Class<? extends Block> clazz;
	
	public AbstractFactoryBuilder(final Class<? extends Block> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public boolean accept(Class<? extends Block> clazz) {
		return this.clazz.isAssignableFrom(clazz);
	}

}
