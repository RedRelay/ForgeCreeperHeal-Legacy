package fr.eyzox.forgecreeperheal.json.adapter;

import java.io.IOException;

import net.minecraft.entity.Entity;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.registry.EntityRegistry;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class ClassAdapter extends AbstractAdapter<Class<?>> {

	@Override
	public void _write(JsonWriter out, Class<?> value) throws IOException {
		out.value(value.getCanonicalName());
		
	}

	@Override
	public Class<?> _read(JsonReader in) throws IOException {
		Class<?> clazz = null;
		String className = in.nextString();
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			ForgeCreeperHeal.getLogger().warn("Unable to find class "+className);
		}
		return clazz;
	}

}
