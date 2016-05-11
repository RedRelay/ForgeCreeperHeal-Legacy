package fr.eyzox.bsc.config.loader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.bsc.exception.ConfigException;

public class ErrorManager implements IErrorManager{

	private final List<ConfigException> errors = new LinkedList<ConfigException>();

	public ErrorManager() {}
	
	@Override
	public void error(ConfigException exception) {
		errors.add(exception);
	}
	
	@Override
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	@Override
	public void output(final PrintWriter out) throws IOException{
		for(final ConfigException e : errors) {
			out.println(e.getMessage());
		}
	}
	
	@Override
	public Collection<ConfigException> getErrors() {
		return errors;
	}

}
