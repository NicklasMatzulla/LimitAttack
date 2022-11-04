package de.playunlimited.limitattack.command;

import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.config.MessagesConfiguration;
import de.playunlimited.limitattack.config.SettingsConfiguration;
import de.playunlimited.limitattack.util.PermissionUtil;
import de.playunlimited.limitcore.command.Command;
import de.playunlimited.limitcore.command.CommandBuilder;
import de.playunlimited.limitcore.command.CommandContext;
import de.playunlimited.limitcore.config.CoreMessagesConfiguration;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
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

        final Command subCommandSurvival = new CommandBuilder("survival")
                .aliases("s", "0")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processGameModeCommand(commandMethod, GameMode.SURVIVAL))
                .usage(this.usage)
                .build();
        final Command subCommandCreative = new CommandBuilder("creative")
                .aliases("c", "1")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processGameModeCommand(commandMethod, GameMode.CREATIVE))
                .usage(this.usage)
                .build();
        final Command subCommandAdventure = new CommandBuilder("adventure")
                .aliases("a", "2")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processGameModeCommand(commandMethod, GameMode.ADVENTURE))
                .usage(this.usage)
                .build();
        final Command subCommandSpectator = new CommandBuilder("spectator")
                .aliases("sp", "3")
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_OTHER)
                .executor(commandMethod -> processGameModeCommand(commandMethod, GameMode.SPECTATOR))
                .usage(this.usage)
                .build();
        final Command baseCommand = new CommandBuilder(settingsConfiguration.COMMAND_GAMEMODE_NAME)
                .aliases(settingsConfiguration.COMMAND_GAMEMODE_ALIASES)
                .description(settingsConfiguration.COMMAND_GAMEMODE_DESCRIPTION)
                .permission(settingsConfiguration.COMMAND_GAMEMODE_PERMISSION_USAGE)
                .executor(this::processBaseCommand)
                .children(subCommandSurvival, subCommandCreative, subCommandAdventure, subCommandSpectator)
                .usage(this.usage)
                .completer(context -> context.playerCompletion(1))
                .minArgs(1)
                .maxArgs(2)
                .build();
        LimitAttack.getInstance().addCommand(baseCommand);
    }

    private void processBaseCommand(@NotNull final CommandContext context) {
        context.sendMessage(this.usage);
    }

    @SuppressWarnings("ConstantConditions")
    private void processGameModeCommand(@NotNull final CommandContext context, @NotNull final GameMode gameMode) {
        final Player player = context.asPlayer();
        Player targetPlayer = null;
        if (context.argAt(0) != null)
            targetPlayer = Bukkit.getPlayer(context.argAt(0));
        String gameModeName = null;
        switch (gameMode) {
            case SURVIVAL -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_SURVIVAL;
            case CREATIVE -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_CREATIVE;
            case ADVENTURE -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_ADVENTURE;
            case SPECTATOR -> gameModeName = this.messagesConfiguration.COMMAND_GAMEMODE_TYPES_SPECTATOR;
        }
        if (targetPlayer != null) {
            if (!player.hasPermission(SettingsConfiguration.getInstance().COMMAND_GAMEMODE_PERMISSION_OTHER)) {
                context.sendMessage(CoreMessagesConfiguration.getInstance().COMMAND_CANNOT_EXECUTE_SUB_COMMAND);
                return;
            }
            if (!allowModify(player, targetPlayer)) {
                context.sendMessage(CoreMessagesConfiguration.getInstance().COMMAND_PLAYER_NOT_EDITABLE
                        .replace("<targetDisplayName>", getDisplayName(targetPlayer)));
                return;
            }
            if (targetPlayer.getGameMode() == gameMode) {
                context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_ALREADY_MODE_OTHER
                        .replace("<gameMode>", gameModeName)
                        .replace("<targetDisplayName>", getDisplayName(targetPlayer)));
                return;
            }
            targetPlayer.setGameMode(gameMode);
            context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_CHANGED_MODE_OTHER
                    .replace("<gameMode>", gameModeName)
                    .replace("<targetDisplayName>", getDisplayName(targetPlayer)));
            context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_CHANGED_MODE_SELF.replace("<gameMode>", gameModeName), targetPlayer);
            return;
        }
        if (player.getGameMode() == gameMode) {
            context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_ALREADY_MODE_SELF.replace("<gameMode>", gameModeName));
            return;
        }
        player.setGameMode(gameMode);
        context.sendMessage(this.messagesConfiguration.COMMAND_GAMEMODE_CHANGED_MODE_SELF.replace("<gameMode>", gameModeName));
    }

    private String getDisplayName(@NotNull final Player player) {
        final User permissionUser = PermissionUtil.getUserFromPlayer(player);
        final Group permissionGroup = PermissionUtil.getPermissionGroup(permissionUser);
        final String permissionGroupPrefix = PermissionUtil.getPermissionGroupPrefix(permissionGroup);
        final String permissionGroupChatColor = PermissionUtil.getPermissionGroupRawChatColor(permissionGroup);
        return permissionGroupPrefix + permissionGroupChatColor + player.getName();
    }

    private boolean allowModify(@NotNull final Player requester, @NotNull final Player target) {
        final User requesterPermissionUser = PermissionUtil.getUserFromPlayer(requester);
        final Group requesterPermissionGroup = PermissionUtil.getPermissionGroup(requesterPermissionUser);
        final int requesterPermissionGroupWeight = PermissionUtil.getPermissionGroupWeight(requesterPermissionGroup);
        final User targetPermissionUser = PermissionUtil.getUserFromPlayer(target);
        final Group targetPermissionGroup = PermissionUtil.getPermissionGroup(targetPermissionUser);
        final int targetPermissionGroupWeight = PermissionUtil.getPermissionGroupWeight(targetPermissionGroup);
        return requesterPermissionGroupWeight > targetPermissionGroupWeight;
    }

}
