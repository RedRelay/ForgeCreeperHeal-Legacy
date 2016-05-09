package fr.eyzox.forgecreeperheal.config.option.validator;

public class BooleanValidator implements IValidator {

	private static final BooleanValidator INSTANCE = new BooleanValidator();
	private BooleanValidator() {}
	
	@Override
	public boolean isValid(String value) throws Exception {
		return value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE");
	}
	
	public static BooleanValidator getInstance() {
		return INSTANCE;
	}

}
