package fr.eyzox.forgecreeperheal.factory.keybuilder;

import net.minecraft.block.Block;

public class BlockKeyBuilder implements IKeyBuilder<Block> {

	private static final BlockKeyBuilder INSTANCE = new BlockKeyBuilder();
	
	private BlockKeyBuilder() {
	}
	
	@Override
	public String convertToString(Block key) {
		return key.getRegistryName().toString();
	}

	@Override
	public Block convertToKey(String keyStr) {
		return Block.getBlockFromName(keyStr);
	}
	
	public static BlockKeyBuilder getInstance() {
		return INSTANCE;
	}

}
