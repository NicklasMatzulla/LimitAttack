package de.playunlimited.limitcore.command.base;

import de.playunlimited.limitcore.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface ITabContext {

    @NotNull Command getCommand();

    @NotNull Plugin getPlugin();

    @NotNull ICommandContext getContext();

    @Nullable CommandSender getSender();

    @NotNull Set<Command> getCommandChildren();

    void playerCompletion(final int index, @NotNull final Predicate<? super Player> predicate);

    void playerCompletion(final int index);

    int getLength();

    boolean length(final int length);

    @NotNull String getPrevious();

    boolean previous(@NotNull final String previousArg);

    void completion(@NotNull final String... completions);

    void completionAt(final int index, @NotNull final String... completions);

    void completionAfter(@NotNull final String previousText, @NotNull final String... completions);

    @NotNull List<String> currentPossibleCompletion();

}
