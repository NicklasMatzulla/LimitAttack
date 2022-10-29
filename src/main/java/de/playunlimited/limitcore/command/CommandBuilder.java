package de.playunlimited.limitcore.command;

import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitcore.command.tabcomplete.TabExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandBuilder {
    private final Command command;

    public CommandBuilder(@NotNull final String name) {
        this.command = new Command(LimitAttack.getInstance(), name);
    }

    public @NotNull CommandBuilder executor(@NotNull final CommandExecutor executor) {
        this.command.setCommandExecutor(executor);
        return this;
    }

    public @NotNull CommandBuilder completer(@NotNull final TabExecutor executor) {
        this.command.setTabExecutor(executor);
        return this;
    }

    public @NotNull CommandBuilder aliases(@NotNull final String... aliases) {
        this.command.setAliases(aliases);
        return this;
    }

    public @NotNull CommandBuilder aliases(@NotNull final Set<String> aliases) {
        this.command.setAliases(aliases);
        return this;
    }

    public @NotNull CommandBuilder description(@NotNull final String description) {
        this.command.setDescription(description);
        return this;
    }

    public @NotNull CommandBuilder usage(@NotNull final String usage) {
        this.command.setUsage(usage);
        return this;
    }

    public @NotNull CommandBuilder permission(@NotNull final String permission) {
        command.setPermission(permission);
        return this;
    }

    public @NotNull CommandBuilder permissionCheck(@NotNull final Predicate<CommandSender> predicate) {
        this.command.setPermissionCheck(predicate);
        return this;
    }

    public @NotNull CommandBuilder minArgs(final int minArgs) {
        this.command.setMinArgs(minArgs);
        return this;
    }

    public @NotNull CommandBuilder maxArgs(final int maxArgs) {
        this.command.setMaxArgs(maxArgs);
        return this;
    }

    public @NotNull CommandBuilder playerOnly() {
        this.command.setPlayerOnly(true);
        return this;
    }

    public @NotNull CommandBuilder consoleOnly() {
        this.command.setConsoleOnly(true);
        return this;
    }

    public @NotNull CommandBuilder children(@NotNull final Command... subCommands) {
        this.command.setChildren(Stream.of(subCommands).collect(Collectors.toSet()));
        return this;
    }

    public @NotNull CommandBuilder children(@NotNull final Set<Command> subCommands) {
        this.command.setChildren(subCommands);
        return this;
    }

    public @NotNull Command build() {
        return this.command;
    }
}
