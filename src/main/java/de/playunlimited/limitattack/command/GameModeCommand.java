package de.playunlimited.limitattack.command;

import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.config.MessagesConfiguration;
import de.playunlimited.limitattack.config.SettingsConfiguration;
import de.playunlimited.limitcore.command.Command;
import de.playunlimited.limitcore.command.CommandBuilder;
import de.playunlimited.limitcore.command.CommandContext;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeCommand {
    private final MessagesConfiguration messagesConfiguration;
    private final String usage;

    public GameModeCommand() {
        final SettingsConfiguration settingsConfiguration = SettingsConfiguration.getInstance();
        this.messagesConfiguration = MessagesConfiguration.getInstance();
        this.usage = this.messagesConfiguration.COMMAND_GAMEMODE_USAGE;

        final Command subCommandPersonalSurvival = new CommandBuilder("survival")
                .aliases("s", "0")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processPersonalGameMode(commandMethod, GameMode.SURVIVAL))
                .usage(this.usage)
                .build();
        final Command subCommandPersonalCreative = new CommandBuilder("creative")
                .aliases("c", "1")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processPersonalGameMode(commandMethod, GameMode.CREATIVE))
                .usage(this.usage)
                .build();
        final Command subCommandPersonalAdventure = new CommandBuilder("adventure")
                .aliases("a", "2")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processPersonalGameMode(commandMethod, GameMode.ADVENTURE))
                .usage(this.usage)
                .build();
        final Command subCommandPersonalSpectator = new CommandBuilder("spectator")
                .aliases("sp", "3")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processPersonalGameMode(commandMethod, GameMode.SPECTATOR))
                .usage(this.usage)
                .build();
        final Command baseCommand = new CommandBuilder(settingsConfiguration.COMMAND_GAMEMODE_NAME)
                .aliases(settingsConfiguration.COMMAND_GAMEMODE_ALIASES)
                .description(settingsConfiguration.COMMAND_GAMEMODE_DESCRIPTION)
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_USAGE)
                .executor(this::processBaseCommand)
                .children(subCommandPersonalSurvival, subCommandPersonalCreative, subCommandPersonalAdventure, subCommandPersonalSpectator)
                .usage(this.usage)
                .minArgs(1)
                .maxArgs(2)
                .build();
        LimitAttack.getInstance().addCommand(baseCommand);
    }

    private void processBaseCommand(@NotNull final CommandContext context) {
        context.sendMessage(this.usage);
    }

    private void processPersonalGameMode(@NotNull final CommandContext context, @NotNull final GameMode gameMode) {
        final Player player = context.asPlayer();
        String gameModeName = null;
        switch (gameMode) {
            case SURVIVAL -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_SURVIVAL;
            case CREATIVE -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_CREATIVE;
            case ADVENTURE -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_ADVENTURE;
            case SPECTATOR -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_SPECTATOR;
        }
        if (player.getGameMode() == gameMode) {
            context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_ALREADY_MODE.replace("<gameMode>", gameModeName));
            return;
        }
        player.setGameMode(gameMode);
        context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_CHANGED_MODE.replace("<gameMode>", gameModeName));
    }

}
