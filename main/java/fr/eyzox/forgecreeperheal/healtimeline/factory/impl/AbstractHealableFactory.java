package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.Block;
import fr.eyzox.forgecreeperheal.healtimeline.factory.IHealableFactory;

public abstract class AbstractHealableFactory implements IHealableFactory {

	private final Class<? extends Block> clazz;
	
	public AbstractHealableFactory(Class<? extends Block> clazz) {
		super();
		this.clazz = clazz;
	}


	@Override
	public Class<? extends Block> accept() {
		return clazz;
	}

}
