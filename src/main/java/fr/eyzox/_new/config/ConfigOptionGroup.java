package fr.eyzox._new.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Altima Agency
 * Created by aduponchel on 09/02/17.
 */
public class ConfigOptionGroup {
    private final String name;
    private final List<ConfigOption<?>> configOptions = new ArrayList<ConfigOption<?>>();

    public ConfigOptionGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected Collection<ConfigOption<?>> getConfigOptions() {
        return configOptions;
    }
}
