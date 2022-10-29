package de.playunlimited.limitcore.command.base;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommandContext implements ICommandContext {
    private final CommandSender sender;
    private final List<String> args;
    private final Plugin plugin;

    public AbstractCommandContext(@NotNull final CommandSender sender, @NotNull final String[] args, @NotNull final Plugin plugin) {
        this.sender = sender;
        this.plugin = plugin;
        this.args = new ArrayList<>(Arrays.asList(args));
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        send(message);
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof Player;
    }

    @Override
    public @Nullable Player asPlayer() {
        return (Player) this.sender;
    }

    @Override
    public boolean isConsole() {
        return this.sender instanceof ConsoleCommandSender;
    }

    @Override
    public @Nullable ConsoleCommandSender asConsole() {
        return (ConsoleCommandSender) this.sender;
    }

    @Override
    public @Nullable CommandSender getSender() {
        return this.sender;
    }

    @Override
    public @NotNull List<String> getArgs() {
        return this.args;
    }

    @Override
    public boolean hasArgs() {
        return !this.args.isEmpty();
    }

    @Override
    public @Nullable String argAt(final int index) {
        if (index < 0 || index >= this.args.size())
            return null;
        return this.args.get(index);
    }

    @Override
    public @Nullable String joinArgs(final int start, final int finish) {
        if (this.args.isEmpty())
            return "";
        return String.join(" ", this.args.subList(start, finish));
    }

    @Override
    public @Nullable String joinArgs(final int start) {
        return joinArgs(start, this.args.size());
    }

    @Override
    public @Nullable String joinArgs() {
        return joinArgs(0);
    }

    @Override
    public void send(@NotNull final String message) {
        sender.sendMessage(message);
    }
}
