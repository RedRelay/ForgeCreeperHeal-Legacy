package fr.eyzox.forgecreeperheal.factory.keybuilder;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameData;

public class BlockKeyBuilder implements IKeyBuilder<Block> {

	private static final BlockKeyBuilder INSTANCE = new BlockKeyBuilder();
	
	private BlockKeyBuilder() {
	}
	
	@Override
	public String convertToString(Block key) {
		return GameData.getBlockRegistry().getNameForObject(key).toString();
	}

	@Override
	public Block convertToKey(String keyStr) {
		return Block.getBlockFromName(keyStr);
	}
	
	public static BlockKeyBuilder getInstance() {
		return INSTANCE;
	}

}
