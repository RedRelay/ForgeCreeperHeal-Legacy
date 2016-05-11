package fr.eyzox.bsc.config.loader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import fr.eyzox.bsc.exception.ConfigException;

public interface IErrorManager {
	public void error(ConfigException exception);
	public boolean hasErrors();
	public void output(final PrintWriter out) throws IOException;
	public Collection<ConfigException> getErrors();
}
