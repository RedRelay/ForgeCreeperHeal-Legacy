package fr.eyzox.forgecreeperheal.config;

public interface IConfigProvider {
	public Config getConfig();
	public void addConfigListener(final IConfigListener listener);
	public void removeConfigListener(final IConfigListener listener);
	public void fireConfigChanged();
	public void loadConfig();
	public void unloadConfig();
}
