package de.playunlimited.limitattack.config;

import de.playunlimited.limitcore.config.CoreMessagesConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfigurationType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MessagesConfiguration extends JsonConfiguration {
    @Getter private static MessagesConfiguration instance;

    public final String COMMAND_GAMEMODE_USAGE;
    public final String COMMAND_GAMEMODE_ALREADY_MODE;
    public final String COMMAND_GAMEMODE_CHANGED_MODE;
    public final String COMMAND_GAMEMODE_TYPES_SURVIVAL;
    public final String COMMAND_GAMEMODE_TYPES_CREATIVE;
    public final String COMMAND_GAMEMODE_TYPES_ADVENTURE;
    public final String COMMAND_GAMEMODE_TYPES_SPECTATOR;

    @SuppressWarnings("ConstantConditions")
    public MessagesConfiguration(@NotNull JsonConfigurationType configurationType, @NotNull File configurationFile, @NotNull String classpathFile) {
        super(configurationType, configurationFile, classpathFile);
        MessagesConfiguration.instance = this;

        final CoreMessagesConfiguration coreMessagesConfiguration = CoreMessagesConfiguration.getInstance();
        this.COMMAND_GAMEMODE_USAGE = ChatColor.translateAlternateColorCodes('&', coreMessagesConfiguration.USAGE + getString("commands.gamemode.usage"));
        this.COMMAND_GAMEMODE_ALREADY_MODE = ChatColor.translateAlternateColorCodes('&', coreMessagesConfiguration.PREFIX + getString("commands.gamemode.messages.alreadyMode"));
        this.COMMAND_GAMEMODE_CHANGED_MODE = ChatColor.translateAlternateColorCodes('&', coreMessagesConfiguration.PREFIX + getString("commands.gamemode.messages.changedMode"));
        this.COMMAND_GAMEMODE_TYPES_SURVIVAL = ChatColor.translateAlternateColorCodes('&', getString("commands.gamemode.messages.types.survival"));
        this.COMMAND_GAMEMODE_TYPES_CREATIVE = ChatColor.translateAlternateColorCodes('&', getString("commands.gamemode.messages.types.creative"));
        this.COMMAND_GAMEMODE_TYPES_ADVENTURE = ChatColor.translateAlternateColorCodes('&', getString("commands.gamemode.messages.types.adventure"));
        this.COMMAND_GAMEMODE_TYPES_SPECTATOR = ChatColor.translateAlternateColorCodes('&', getString("commands.gamemode.messages.types.spectator"));
    }
}
