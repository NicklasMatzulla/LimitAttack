package de.playunlimited.limitattack.config;

import de.playunlimited.limitcore.config.util.JsonConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfigurationType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public class SettingsConfiguration extends JsonConfiguration {
    @Getter private static SettingsConfiguration instance;

    public final String FEATURES_CHAT_PERMISSION_COLOR_CHAT;
    public final String COMMAND_GAMEMODE_NAME;
    public final Set<String> COMMAND_GAMEMODE_ALIASES;
    public final String COMMAND_GAMEMODE_DESCRIPTION;
    public final String COMMAND_GAMEMODE_PERMISSION_USAGE;
    public final String COMMAND_GAMEMODE_PERMISSION_OTHER;

    public SettingsConfiguration(@NotNull JsonConfigurationType configurationType, @NotNull File configurationFile, @NotNull String classpathFile) {
        super(configurationType, configurationFile, classpathFile);
        SettingsConfiguration.instance = this;

        this.FEATURES_CHAT_PERMISSION_COLOR_CHAT = getString("features.chat.permissions.colorChat");
        this.COMMAND_GAMEMODE_NAME = getString("commands.gamemode.name");
        this.COMMAND_GAMEMODE_ALIASES = getStringSet("commands.gamemode.aliases");
        this.COMMAND_GAMEMODE_DESCRIPTION = getString("commands.gamemode.description");
        this.COMMAND_GAMEMODE_PERMISSION_USAGE = getString("commands.gamemode.permissions.usage");
        this.COMMAND_GAMEMODE_PERMISSION_OTHER = getString("commands.gamemode.permissions.other");
    }
}
