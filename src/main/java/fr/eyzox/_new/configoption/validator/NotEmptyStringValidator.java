package fr.eyzox._new.configoption.validator;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public class NotEmptyStringValidator extends NotNullValidator<String>{
    @Override
    public boolean isValid(ConfigOption<?> config, String obj) throws InvalidValueException {
        final boolean isNull = super.isValid(config, obj);
        if(!isNull && obj.isEmpty()) {
            throw new InvalidValueException(obj, "Can not be empty");
        }
        return true;
    }
}
