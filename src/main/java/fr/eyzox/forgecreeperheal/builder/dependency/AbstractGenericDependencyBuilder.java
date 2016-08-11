package fr.eyzox.forgecreeperheal.builder.dependency;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;

public abstract class AbstractGenericDependencyBuilder implements IDependencyBuilder{
	
	private final List<Class<? extends Block>> registeredClasses = new LinkedList<Class<? extends Block>>();

	@Override
	public boolean accept(Block in) {
		for(final Class<? extends Block> clazz : registeredClasses) {
			if(clazz.isAssignableFrom(in.getClass())) {
				return true;
			}
		}
		return false;
	}
	
	public List<Class<? extends Block>> getRegisteredClasses() {
		return registeredClasses;
	}
	
	public void register(final Class<? extends Block> clazz) {
		this.registeredClasses.add(clazz);
	}
	
	public boolean unregister(final Class<? extends Block> clazz) {
		return this.registeredClasses.remove(clazz);
	}

}
