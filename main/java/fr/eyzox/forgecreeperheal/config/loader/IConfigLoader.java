package fr.eyzox.forgecreeperheal.config.loader;

import fr.eyzox.forgecreeperheal.config.Config;

public interface IConfigLoader {
	public void load(final Config config) throws Exception;
	public void save(final Config config) throws Exception;
	public IErrorManager getErrorManager();
}
