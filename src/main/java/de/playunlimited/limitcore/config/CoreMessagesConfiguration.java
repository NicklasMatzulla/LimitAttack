package de.playunlimited.limitcore.config;

import de.playunlimited.limitcore.config.util.JsonConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfigurationType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CoreMessagesConfiguration extends JsonConfiguration {
    @Getter private static CoreMessagesConfiguration instance;

    public final String PREFIX;
    public final String USAGE;
    public final String NO_PERMISSION;
    public final String COMMAND_PLAYER_ONLY;
    public final String COMMAND_CONSOLE_ONLY;
    public final String COMMAND_PLAYER_NOT_FOUND;

    @SuppressWarnings("ConstantConditions")
    public CoreMessagesConfiguration(@NotNull JsonConfigurationType configurationType, @NotNull File configurationFile, @NotNull String classpathFile) {
        super(configurationType, configurationFile, classpathFile);
        CoreMessagesConfiguration.instance = this;
        JsonConfiguration.coreMessagesConfiguration = this;

        this.PREFIX = getString("prefix");
        this.USAGE = getString("usage");
        this.NO_PERMISSION = getString("noPermission");
        this.COMMAND_PLAYER_ONLY = getString("command.playerOnly");
        this.COMMAND_CONSOLE_ONLY = getString("command.consoleOnly");
        this.COMMAND_PLAYER_NOT_FOUND = getString("command.playerNotFound");
    }
}
