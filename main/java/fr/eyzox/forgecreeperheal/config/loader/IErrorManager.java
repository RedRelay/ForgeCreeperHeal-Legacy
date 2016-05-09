package fr.eyzox.forgecreeperheal.config.loader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealException;

public interface IErrorManager {
	public void error(ForgeCreeperHealException exception);
	public boolean hasErrors();
	public void output(final PrintWriter out) throws IOException;
	public Collection<ForgeCreeperHealException> getErrors();
}
