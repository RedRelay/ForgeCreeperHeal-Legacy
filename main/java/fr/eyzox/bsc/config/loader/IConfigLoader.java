package fr.eyzox.bsc.config.loader;

import fr.eyzox.bsc.config.Config;

public interface IConfigLoader {
	public void load(final Config config) throws Exception;
	public void save(final Config config) throws Exception;
	public IErrorManager getErrorManager();
}
