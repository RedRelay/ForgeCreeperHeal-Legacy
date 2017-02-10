package _new_old;

import java.util.HashMap;
import java.util.Map;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.IFastConfigEditor;

public class ConfigProvider<R, W> {
	
	private class Wrapper {
		ConfigOption<?> configOption;
		ISerializer<R, W, ?> serializer;
		IFastConfigUpdater fastConfigEditor;
		
		Wrapper(ConfigOption<?> configOption, ISerializer<R, W, ?> serializer, IFastConfigUpdater fastConfigEditor) {
			this.configOption = configOption;
			this.serializer = serializer;
			this.fastConfigEditor = fastConfigEditor;
		}
		
		
	}
	
	private Map<String, Wrapper> registeredConfigOptions = new HashMap<String, Wrapper>();
	
	public <T> void register(ConfigOption<T> configOption, ISerializer<R, W, ?> serializer) {
		this.register(configOption, serializer, null);
	}
	
	public <T> void register(ConfigOption<T> configOption, ISerializer<R, W, ?> serializer, IFastConfigUpdater fastConfigEditor) {
		this.registeredConfigOptions.put(configOption.getName(), new Wrapper(configOption, serializer, fastConfigEditor));
	}
	
	public void unregister(String name) {
		this.registeredConfigOptions.remove(name);
	}
}
