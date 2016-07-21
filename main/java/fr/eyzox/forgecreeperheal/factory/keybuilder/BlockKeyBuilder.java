package fr.eyzox.forgecreeperheal.factory.keybuilder;

import net.minecraft.block.Block;

public class BlockKeyBuilder implements IKeyBuilder<Block> {

	@Override
	public String convertToString(Block key) {
		return key.getRegistryName().toString();
	}

	@Override
	public Block convertToKey(String keyStr) {
		return Block.getBlockFromName(keyStr);
	}

}
