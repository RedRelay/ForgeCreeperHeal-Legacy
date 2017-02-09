package fr.eyzox._new.config;

/**
 * Altima Agency
 * Created by aduponchel on 09/02/17.
 */
public class NoSerializerException extends RuntimeException{
    public NoSerializerException(ConfigOption<?> co) {
        super("Unable to find serializer for "+co.getClass().getSimpleName()+" "+co.getName()+" : getValue() -> "+co.getValue().getClass().getName());
    }
}
