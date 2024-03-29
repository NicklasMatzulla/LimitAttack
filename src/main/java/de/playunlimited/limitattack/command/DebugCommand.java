package de.playunlimited.limitattack.command;

import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.chat.ScoreboardTeam;
import de.playunlimited.limitcore.command.Command;
import de.playunlimited.limitcore.command.CommandBuilder;
import de.playunlimited.limitcore.command.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand {

    public DebugCommand() {
        final Command baseCommand = new CommandBuilder("debug")
                .playerOnly()
                .permission("limitattack.commands.debug")
                .executor(this::processBaseCommand)
                .build();
        LimitAttack.getInstance().addCommand(baseCommand);
    }

    @SuppressWarnings("ConstantConditions")
    private void processBaseCommand(@NotNull final CommandContext context) {
        final Player player = context.asPlayer();
        final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(Bukkit.getPlayer("StaticDev").getUniqueId()).get("ScoreboardTeam");
        if (context.argAt(0).equalsIgnoreCase("1")) {
            scoreboardTeam.unregisterViewerScoreboardTeam(player);
            context.sendMessage("done step 1");
        } else if (context.argAt(0).equalsIgnoreCase("2")) {
            scoreboardTeam.setViewerPrimaryScoreboardTeam(player, "vanish");
            context.sendMessage("done step 2");
        }
    }

}
