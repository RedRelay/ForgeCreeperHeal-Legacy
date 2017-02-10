package fr.eyzox._new.configoption;

public class ConfigOption<T> {
	private final String name;
	private T value;
	
	public ConfigOption(String name, T defaultValue) {
		this.name = name;
		this.value = defaultValue;
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
