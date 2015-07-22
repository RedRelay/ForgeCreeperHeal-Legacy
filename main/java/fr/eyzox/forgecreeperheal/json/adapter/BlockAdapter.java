package fr.eyzox.forgecreeperheal.json.adapter;

import java.io.IOException;

import net.minecraft.block.Block;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.registry.GameData;

public class BlockAdapter extends AbstractAdapter<Block> {

	@Override
	public void _write(JsonWriter out, Block value) throws IOException {
		String blockName = GameData.getBlockRegistry().getNameForObject(value);
		if(blockName == null) {
			out.nullValue();
		}else {
			out.value(blockName);
		}
		
	}

	@Override
	public Block _read(JsonReader in) throws IOException {
		return Block.getBlockFromName(in.nextString());
	}

}
