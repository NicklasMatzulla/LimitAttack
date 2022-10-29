package de.playunlimited.limitcore.command;

import de.playunlimited.limitcore.command.base.AbstractCommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandContext extends AbstractCommandContext {

    public CommandContext(@NotNull final CommandSender sender, @NotNull final String[] args, @NotNull final Plugin plugin) {
        super(sender, args, plugin);
    }
}
