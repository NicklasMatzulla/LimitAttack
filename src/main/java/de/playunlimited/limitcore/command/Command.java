package de.playunlimited.limitcore.command;

import com.google.common.collect.Lists;
import de.playunlimited.limitcore.command.base.ICommandContext;
import de.playunlimited.limitcore.command.base.ITabContext;
import de.playunlimited.limitcore.command.tabcomplete.TabContext;
import de.playunlimited.limitcore.command.tabcomplete.TabExecutor;
import de.playunlimited.limitcore.config.CoreMessagesConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Command {
    private Predicate<CommandSender> permissionCheck;
    private CommandExecutor commandExecutor;
    private boolean playerOnly = false;
    private boolean consoleOnly = false;
    private String description = "";
    private Set<Command> children;
    private TabExecutor tabExecutor;
    private final Plugin plugin;
    private Set<String> aliases;
    private String usage = "";
    private String permission;
    private int minArgs = -1;
    private int maxArgs = -1;
    private String name;

    public Command(@NotNull final Plugin plugin, @NotNull final String name) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = new HashSet<>();
        this.children = new HashSet<>();
    }

    @SuppressWarnings("ConstantConditions")
    public void execute(@NotNull final ICommandContext context) {
        if (!checkChildren(context)) {
            final CoreMessagesConfiguration coreMessagesConfiguration = CoreMessagesConfiguration.getInstance();
            if (context.isConsole() && this.playerOnly) {
                context.sendMessage(coreMessagesConfiguration.COMMAND_PLAYER_ONLY);
                return;
            }
            if (context.isPlayer() && this.consoleOnly) {
                context.sendMessage(coreMessagesConfiguration.COMMAND_CONSOLE_ONLY);
                return;
            }
            if (this.permission != null && !context.getSender().hasPermission(this.permission)) {
                context.sendMessage(coreMessagesConfiguration.NO_PERMISSION);
                return;
            }
            if (this.permissionCheck != null && !this.permissionCheck.test(context.getSender())) {
                context.sendMessage(coreMessagesConfiguration.NO_PERMISSION);
                return;
            }
            if (this.maxArgs > -1 && context.getArgs().size() > this.maxArgs) {
                if (usage != null)
                    context.sendMessage(this.usage);
                return;
            }
            if (this.minArgs > -1 && context.getArgs().size() < this.minArgs) {
                if (this.usage != null)
                    context.sendMessage(this.usage);
                return;
            }
            this.commandExecutor.execute((CommandContext) context);
        }
    }

    public @Nullable List<String> completer(@NotNull final ITabContext tabContext) {
        final List<String> sub = Lists.newArrayList();
        if (this.tabExecutor == null) {
            if (tabContext.getCommandChildren().isEmpty())
                return null;
            if (tabContext.getLength() == 0) {
                final List<String> children = new ArrayList<>();
                tabContext.getCommandChildren().forEach(child -> children.add(child.getName()));
                return children;
            }
            return null;
        }
        this.tabExecutor.complete((TabContext) tabContext);
        for (final String completion : tabContext.currentPossibleCompletion()) {
            if (completion.toLowerCase().startsWith(tabContext.getContext().argAt(tabContext.getContext().getArgs().size() - 1)))
                sub.add(completion);
        }
        return sub;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean checkChildren(@NotNull final ICommandContext context) {
        if (children.isEmpty())
            return false;
        if (!context.hasArgs())
            return false;
        for (final Command childCommand : this.children) {
            if (childCommand.matchesCommand(context.argAt(0))) {
                context.getArgs().remove(0);
                childCommand.execute(context);
                return true;
            }
        }
        return false;
    }

    public @NotNull CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }

    public void setCommandExecutor(@NotNull final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public boolean matchesCommand(@NotNull String input) {
        input = input.toLowerCase();
        return (input.equals(name) || this.aliases.contains(input));
    }

    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    public @NotNull Set<String> getAliases() {
        return this.aliases;
    }

    public void setAliases(@NotNull final Set<String> aliases) {
        this.aliases = aliases;
    }

    public void setAliases(@NotNull final String... aliases) {
        this.aliases = Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet());
    }

    public boolean hasAliases() {
        return this.aliases.size() > 0;
    }

    public @NotNull String getDescription() {
        return this.description;
    }

    public void setDescription(@NotNull final String description) {
        this.description = description;
    }

    public @NotNull String getUsage() {
        return this.usage;
    }

    public void setUsage(@NotNull final String usage) {
        this.usage = usage;
    }

    public @NotNull String getPermission() {
        return this.permission;
    }

    public void setPermission(@NotNull final String permission) {
        this.permission = permission;
    }

    public void setPermissionCheck(@NotNull final Predicate<CommandSender> permissionCheck) {
        this.permissionCheck = permissionCheck;
    }

    public int getMinArgs() {
        return this.minArgs;
    }

    public void setMinArgs(final int minArgs) {
        this.minArgs = minArgs;
    }

    public int getMaxArgs() {
        return this.maxArgs;
    }

    public void setMaxArgs(final int maxArgs) {
        this.maxArgs = maxArgs;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public boolean isConsoleOnly() {
        return this.consoleOnly;
    }

    public void setConsoleOnly(final boolean consoleOnly) {
        this.consoleOnly = consoleOnly;
    }

    public @NotNull Set<Command> getChildren() {
        return this.children;
    }

    public void setChildren(@NotNull final Set<Command> children) {
        this.children = children;
    }

    public @NotNull TabExecutor getTabExecutor() {
        return this.tabExecutor;
    }

    public void setTabExecutor(@NotNull final TabExecutor tabExecutor) {
        this.tabExecutor = tabExecutor;
    }
}
