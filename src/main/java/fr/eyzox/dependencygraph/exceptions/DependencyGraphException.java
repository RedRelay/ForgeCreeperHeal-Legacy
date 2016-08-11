package fr.eyzox.dependencygraph.exceptions;

public class DependencyGraphException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4337395305520296735L;

	public DependencyGraphException() {
		super();
	}

	protected DependencyGraphException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public DependencyGraphException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DependencyGraphException(String arg0) {
		super(arg0);
	}

	public DependencyGraphException(Throwable arg0) {
		super(arg0);
	}

}
