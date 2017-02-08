package fr.eyzox._new.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigProvider<S extends ISerializer> {
	
	private class Wrapper {
		ConfigOption<?> configOption;
		S serializer;
		IFastConfigEditor fastConfigEditor;
		
		Wrapper(ConfigOption<?> configOption, S serializer, IFastConfigEditor fastConfigEditor) {
			this.configOption = configOption;
			this.serializer = serializer;
			this.fastConfigEditor = fastConfigEditor;
		}
		
		
	}
	
	private Map<String, Wrapper> registeredConfigOptions = new HashMap<String, Wrapper>();
	
	public <T> void register(ConfigOption<T> configOption, S serializer) {
		this.register(configOption, serializer, null);
	}
	
	public <T> void register(ConfigOption<T> configOption, S serializer, IFastConfigEditor fastConfigEditor) {
		this.registeredConfigOptions.put(configOption.getName(), new Wrapper(configOption, serializer, fastConfigEditor));
	}
	
	public void unregister(String name) {
		this.registeredConfigOptions.remove(name);
	}
}
