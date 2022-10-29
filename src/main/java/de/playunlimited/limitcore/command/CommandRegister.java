package de.playunlimited.limitcore.command;

import de.playunlimited.limitcore.command.tabcomplete.TabContext;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CommandRegister extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    private final Command command;
    private final Plugin plugin;

    public CommandRegister(@NotNull final Command command, @NotNull final Plugin plugin) {
        super(command.getName());
        this.plugin = plugin;
        this.command = command;
        this.description = command.getDescription();
        this.usageMessage = command.getUsage();
        this.setAliases(new ArrayList<>(command.getAliases()));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String commandLabel, @NotNull final String[] args) {
        if (this.command.matchesCommand(commandLabel)) {
            this.command.execute(new CommandContext(sender, args, this.plugin));
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String alias, @NotNull final String[] args) throws IllegalArgumentException {
        return this.command.completer(new TabContext(this.plugin, this.command, new CommandContext(sender, args, this.plugin)));
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
