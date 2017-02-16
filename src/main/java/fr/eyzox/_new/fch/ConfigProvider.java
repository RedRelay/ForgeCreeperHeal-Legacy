package fr.eyzox._new.fch;

import fr.eyzox._new.configoption.ConfigOptionGroup;

import java.io.File;

public class ConfigProvider {

    private final FCHConfigBuilder config = new FCHConfigBuilder();
    private final File configFile;

    public ConfigProvider(File configFile) {
        this.configFile = configFile;
    }

    public ConfigOptionGroup getConfig() {
        return config.build();
    }

    public void load() {
        config.load(configFile);
    }

    public void save() {
        config.save(configFile);
    }

}
