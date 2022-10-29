package de.playunlimited.limitcore.command.tabcomplete;

import de.playunlimited.limitcore.command.Command;
import de.playunlimited.limitcore.command.base.AbstractTabContext;
import de.playunlimited.limitcore.command.base.ICommandContext;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;


public class TabContext extends AbstractTabContext {

    public TabContext(@NotNull final Plugin plugin, @NotNull final Command command, @NotNull final ICommandContext commandContext) {
        super(plugin, command, commandContext);
    }
}
