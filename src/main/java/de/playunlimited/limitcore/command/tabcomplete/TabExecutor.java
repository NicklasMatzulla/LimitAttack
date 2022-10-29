package de.playunlimited.limitcore.command.tabcomplete;

import org.jetbrains.annotations.NotNull;

public interface TabExecutor {

    void complete(@NotNull final TabContext context);

}
