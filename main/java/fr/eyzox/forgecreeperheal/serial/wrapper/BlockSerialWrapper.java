package fr.eyzox.forgecreeperheal.serial.wrapper;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.builder.blockdata.IBlockDataBuilder;
import fr.eyzox.forgecreeperheal.factory.Factory;
import net.minecraft.block.Block;

public class BlockSerialWrapper implements ISerialWrapper<Class<? extends Block>>{

	private String clazz;
	
	public BlockSerialWrapper() {}
	
	public void initialize(final String clazz) {
		this.clazz = clazz;
	}

	@Override
	public String getFactoryKey() {
		return clazz;
	}

	@Override
	public Factory<Class<? extends Block>, IBlockDataBuilder> getFactory() {
		return ForgeCreeperHeal.getBlockDataFactory();
	}

}
