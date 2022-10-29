package de.playunlimited.limitcore.command.base;

import de.playunlimited.limitcore.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class AbstractTabContext implements ITabContext {

    private final Plugin plugin;
    private final Command command;
    private final ICommandContext commandContext;
    private final List<String> possible = new ArrayList<>();

    public AbstractTabContext(@NotNull final Plugin plugin, @NotNull final Command command, @NotNull final ICommandContext commandContext) {
        this.plugin = plugin;
        this.command = command;
        this.commandContext = commandContext;
    }

    @Override
    public @NotNull Command getCommand() {
        return command;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull ICommandContext getContext() {
        return this.commandContext;
    }

    @Override
    public @Nullable CommandSender getSender() {
        return this.commandContext.getSender();
    }

    @Override
    public @NotNull Set<Command> getCommandChildren() {
        return this.command.getChildren();
    }

    @Override
    public int getLength() {
        return this.commandContext.getArgs().size() - 1;
    }

    @Override
    public boolean length(final int length) {
        return getLength() == length;
    }

    @Override
    public @NotNull String getPrevious() {
        return this.commandContext.getArgs().get(getLength());
    }

    @Override
    public boolean previous(@NotNull final String previousArg) {
        return this.getPrevious().matches(previousArg);
    }

    @Override
    public void playerCompletion(final int index, @NotNull final Predicate<? super Player> predicate) {
        if (length(index)) {
            this.possible.clear();
            Bukkit.getOnlinePlayers().stream().filter(predicate).forEach(p -> this.possible.add(p.getName()));
        }
    }

    @Override
    public void playerCompletion(final int index) {
        if (length(index)) {
            this.possible.clear();
            Bukkit.getOnlinePlayers().forEach(p -> this.possible.add(p.getName()));
        }
    }

    @Override
    public void completion(@NotNull final String... completions) {
        this.possible.clear();
        this.possible.addAll(Arrays.asList(completions));
    }

    @Override
    public void completionAt(final int index, @NotNull final String... completions) {
        if (length(index)) {
            this.possible.clear();
            this.possible.addAll(Arrays.asList(completions));
        }
    }

    @Override
    public void completionAfter(@NotNull final String previousText, @NotNull final String... completions) {
        if (previous(previousText)) {
            this.possible.clear();
            this.possible.addAll(Arrays.asList(completions));
        }
    }

    @Override
    public @NotNull List<String> currentPossibleCompletion() {
        return this.possible;
    }

}
