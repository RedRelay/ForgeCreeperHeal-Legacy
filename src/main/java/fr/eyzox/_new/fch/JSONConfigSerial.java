package fr.eyzox._new.fch;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.ConfigOptionGroup;
import fr.eyzox.bsc.config.loader.AbstractFileConfigLoader;

public class JSONConfigSerial extends AbstractFileConfigLoader{

	private final Gson gson;

	public JSONConfigSerial(File file) {
		super(file);
		final GsonBuilder builder = new GsonBuilder();
	    builder.registerTypeHierarchyAdapter(ConfigOption.class, new JsonSerializer<ConfigOption<?>>() {
	        @Override
	        public JsonElement serialize(ConfigOption<?> container, Type type, JsonSerializationContext ctx) {
	            return ctx.serialize(container.getValue());
	        }
	    });
	    gson = builder.create();
	}
	
    public void load(FileReader fr, ConfigOptionGroup config) {
    	final JsonParser parser = new JsonParser();
        JsonObject jsonRoot = parser.parse(fr).getAsJsonObject();
        for(ConfigOption<?> entry : config.getValue().values()) {
        	JsonElement e = jsonRoot.get(entry.getName());
        	if(e != null) {
        		entry.
        	}
        }
        
    }

    public void save(FileWriter fw, ConfigOptionGroup config) {
		gson.toJson(config, fw);
    	
    }
	


}
