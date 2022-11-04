package de.playunlimited.limitattack.config;

import de.playunlimited.limitcore.config.CoreMessagesConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfigurationType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MessagesConfiguration extends JsonConfiguration {
    @Getter private static MessagesConfiguration instance;

    public final String FEATURES_CHAT_FORMAT;
    public final String FEATURES_TABLIST_HEADER;
    public final String FEATURES_TABLIST_FOOTER;
    public final String COMMAND_GAMEMODE_USAGE;
    public final String COMMAND_GAMEMODE_ALREADY_MODE_SELF;
    public final String COMMAND_GAMEMODE_ALREADY_MODE_OTHER;
    public final String COMMAND_GAMEMODE_CHANGED_MODE_SELF;
    public final String COMMAND_GAMEMODE_CHANGED_MODE_OTHER;
    public final String COMMAND_GAMEMODE_TYPES_SURVIVAL;
    public final String COMMAND_GAMEMODE_TYPES_CREATIVE;
    public final String COMMAND_GAMEMODE_TYPES_ADVENTURE;
    public final String COMMAND_GAMEMODE_TYPES_SPECTATOR;

    public MessagesConfiguration(@NotNull JsonConfigurationType configurationType, @NotNull File configurationFile, @NotNull String classpathFile) {
        super(configurationType, configurationFile, classpathFile);
        MessagesConfiguration.instance = this;

        final CoreMessagesConfiguration coreMessagesConfiguration = CoreMessagesConfiguration.getInstance();
        this.FEATURES_CHAT_FORMAT = getString("features.chat.format");
        this.FEATURES_TABLIST_HEADER = getString("features.tablist.header");
        this.FEATURES_TABLIST_FOOTER = getString("features.tablist.footer");
        this.COMMAND_GAMEMODE_USAGE = coreMessagesConfiguration.USAGE + getString("commands.gamemode.usage");
        this.COMMAND_GAMEMODE_ALREADY_MODE_SELF = coreMessagesConfiguration.PREFIX + getString("commands.gamemode.messages.alreadyMode.self");
        this.COMMAND_GAMEMODE_ALREADY_MODE_OTHER = coreMessagesConfiguration.PREFIX + getString("commands.gamemode.messages.alreadyMode.other");
        this.COMMAND_GAMEMODE_CHANGED_MODE_SELF = coreMessagesConfiguration.PREFIX + getString("commands.gamemode.messages.changedMode.self");
        this.COMMAND_GAMEMODE_CHANGED_MODE_OTHER = coreMessagesConfiguration.PREFIX + getString("commands.gamemode.messages.changedMode.other");
        this.COMMAND_GAMEMODE_TYPES_SURVIVAL = getString("commands.gamemode.messages.types.survival");
        this.COMMAND_GAMEMODE_TYPES_CREATIVE = getString("commands.gamemode.messages.types.creative");
        this.COMMAND_GAMEMODE_TYPES_ADVENTURE = getString("commands.gamemode.messages.types.adventure");
        this.COMMAND_GAMEMODE_TYPES_SPECTATOR = getString("commands.gamemode.messages.types.spectator");
    }
}
