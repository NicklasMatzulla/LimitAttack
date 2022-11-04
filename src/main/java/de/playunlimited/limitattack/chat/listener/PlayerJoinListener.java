package de.playunlimited.limitattack.chat.listener;

import de.myzelyam.api.vanish.VanishAPI;
import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.chat.ScoreboardTeam;
import de.playunlimited.limitattack.chat.TablistStyle;
import de.playunlimited.limitcore.CacheProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("pv.joinvanished")) {
            final ScoreboardTeam scoreboardTeam = new ScoreboardTeam(player);
            final ScoreboardTeam.CategorizedScoreboardTeam defaultScoreboardTeam = scoreboardTeam.getDefaultScoreboardTeam();
            final CacheProvider cacheProvider = LimitAttack.getCacheProvider();
            for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                defaultScoreboardTeam.addViewablePlayer(onlinePlayers);
                if (player != onlinePlayers) {
                    final ScoreboardTeam onlinePlayerScoreboardTeam = (ScoreboardTeam) cacheProvider.getUserCache(onlinePlayers.getUniqueId()).get("ScoreboardTeam");
                    final ScoreboardTeam.CategorizedScoreboardTeam onlinePlayerDefaultScoreboardTeam = onlinePlayerScoreboardTeam.getDefaultScoreboardTeam();
                    onlinePlayerDefaultScoreboardTeam.addViewablePlayer(player);
                }
            }
            TablistStyle.updateTablist();
        } else {
            final ScoreboardTeam scoreboardTeam = new ScoreboardTeam(player);
            final ScoreboardTeam.CategorizedScoreboardTeam vanishScoreboardTeam = scoreboardTeam.getVanishScoreboardTeam();
            final CacheProvider cacheProvider = LimitAttack.getCacheProvider();
            cacheProvider.getUserCache(player.getUniqueId()).set("SetPlayerJoinVanishedScoreboard", true);
            for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                if (player != onlinePlayers) {
                    if (VanishAPI.canSee(onlinePlayers, player))
                        vanishScoreboardTeam.addViewablePlayer(onlinePlayers);
                    if (VanishAPI.isInvisible(onlinePlayers)) {
                        if (VanishAPI.canSee(player, onlinePlayers)) {
                            final ScoreboardTeam onlinePlayerScoreboardTeam = (ScoreboardTeam) cacheProvider.getUserCache(onlinePlayers.getUniqueId()).get("ScoreboardTeam");
                            final ScoreboardTeam.CategorizedScoreboardTeam onlinePlayerVanishScoreboardTeam = onlinePlayerScoreboardTeam.getVanishScoreboardTeam();
                            onlinePlayerVanishScoreboardTeam.addViewablePlayer(player);
                        }
                    } else {
                        final ScoreboardTeam onlinePlayerScoreboardTeam = (ScoreboardTeam) cacheProvider.getUserCache(onlinePlayers.getUniqueId()).get("ScoreboardTeam");
                        final ScoreboardTeam.CategorizedScoreboardTeam onlinePlayerDefaultScoreboardTeam = onlinePlayerScoreboardTeam.getDefaultScoreboardTeam();
                        onlinePlayerDefaultScoreboardTeam.addViewablePlayer(player);
                    }
                }
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(LimitAttack.getInstance(), () -> vanishScoreboardTeam.addViewablePlayer(player), 50);
            TablistStyle.updateTablist();
        }
    }

}
