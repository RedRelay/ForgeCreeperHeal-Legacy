package fr.eyzox._new.configoption.validator;

import fr.eyzox._new.configoption.ConfigOption;
import fr.eyzox._new.configoption.exceptions.InvalidValueException;

public class NotNullValidator<T> implements IValidator<T> {
    @Override
    public boolean isValid(ConfigOption<?> config, T obj) throws InvalidValueException {
        if(obj == null) throw new InvalidValueException(obj.getClass(), "null value not allowed");
        return true;
    }
}
