package de.playunlimited.limitcore.command;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandExecutor {

    void execute(@NotNull final CommandContext commandMethod);

}
