package de.playunlimited.limitattack.chat.listener;

import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.chat.ScoreboardTeam;
import de.playunlimited.limitattack.chat.TablistStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(player.getUniqueId()).get("ScoreboardTeam");
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
            scoreboardTeam.unregisterViewerScoreboardTeam(onlinePlayer);
        TablistStyle.updateTablist();
    }

}
