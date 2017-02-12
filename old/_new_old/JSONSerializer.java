package _new_old;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public interface JSONSerializer<T> extends ISerializer<JsonReader, JsonWriter,T>{

}
