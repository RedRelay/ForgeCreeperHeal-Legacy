package fr.eyzox._new.config;

public class ConfigOption<T> {
	private final String name;
	private String description;
	private T value;
	
	public ConfigOption(String name, T defaultValue) {
		this.name = name;
		this.value = defaultValue;
		if(name != null) {
			this.description = "fch.config."+name.toLowerCase()+".desc";
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

}
