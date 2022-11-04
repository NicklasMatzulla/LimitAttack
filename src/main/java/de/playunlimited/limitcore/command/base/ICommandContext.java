package de.playunlimited.limitcore.command.base;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ICommandContext {

    @NotNull Plugin getPlugin();

    void sendMessage(@NotNull final String message);

    boolean isPlayer();

    @Nullable Player asPlayer();

    boolean isConsole();

    @Nullable ConsoleCommandSender asConsole();

    @Nullable CommandSender getSender();

    @NotNull List<String> getArgs();

    boolean hasArgs();

    @Nullable String argAt(final int index);

    @Nullable String joinArgs(final int start, final int finish);

    @Nullable String joinArgs(final int start);

    @Nullable String joinArgs();

    void send(@NotNull Component component);
}
