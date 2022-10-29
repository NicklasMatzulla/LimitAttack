package de.playunlimited.limitcore.config;

import de.playunlimited.limitcore.config.util.JsonConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfigurationType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CoreSettingsConfiguration extends JsonConfiguration {

    public CoreSettingsConfiguration(@NotNull JsonConfigurationType configurationType, @NotNull File configurationFile, @NotNull String classpathFile) {
        super(configurationType, configurationFile, classpathFile);
        JsonConfiguration.coreSettingsConfiguration = this;
    }
}
