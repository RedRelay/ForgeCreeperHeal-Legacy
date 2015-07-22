package fr.eyzox.forgecreeperheal.json.adapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public abstract class AbstractAdapter<T> extends TypeAdapter<T>{

	@Override
	public void write(JsonWriter out, T value) throws IOException {
		if(value == null) out.nullValue();
		else _write(out, value);
		
	}

	@Override
	public T read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}else {
			return _read(in);
		}
	}
	
	protected abstract void _write(JsonWriter out, T value) throws IOException;
	protected abstract T _read(JsonReader in) throws IOException;

}
