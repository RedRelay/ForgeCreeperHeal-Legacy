package fr.eyzox.forgecreeperheal.config.loader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;

public class ErrorManager implements IErrorManager{

	private final List<ForgeCreeperHealException> errors = new LinkedList<ForgeCreeperHealException>();

	public ErrorManager() {}
	
	@Override
	public void error(ForgeCreeperHealException exception) {
		errors.add(exception);
	}
	
	@Override
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	@Override
	public void output(final PrintWriter out) throws IOException{
		for(final ForgeCreeperHealException e : errors) {
			out.println(e.getMessage());
		}
	}
	
	@Override
	public Collection<ForgeCreeperHealException> getErrors() {
		return errors;
	}

}
