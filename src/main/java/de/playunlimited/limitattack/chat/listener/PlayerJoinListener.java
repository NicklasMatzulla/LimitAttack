package de.playunlimited.limitattack.chat.listener;

import de.myzelyam.api.vanish.PostPlayerHideEvent;
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

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPlayerJoinEvent(@NotNull final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("pv.joinvanished")) {
            final ScoreboardTeam scoreboardTeam = new ScoreboardTeam(player);
            final ScoreboardTeam.CategorizedScoreboardTeam defaultScoreboardTeam = scoreboardTeam.getCategorizedScoreboardTeam("default");
            final CacheProvider cacheProvider = LimitAttack.getCacheProvider();
            for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                defaultScoreboardTeam.addViewablePlayer(onlinePlayers);
                if (player != onlinePlayers) {
                    final ScoreboardTeam onlinePlayerScoreboardTeam = (ScoreboardTeam) cacheProvider.getUserCache(onlinePlayers.getUniqueId()).get("ScoreboardTeam");
                    final ScoreboardTeam.CategorizedScoreboardTeam onlinePlayerDefaultScoreboardTeam = onlinePlayerScoreboardTeam.getCategorizedScoreboardTeam("default");
                    onlinePlayerDefaultScoreboardTeam.addViewablePlayer(player);
                }
            }
            TablistStyle.updateTablist();
        } else {
            new ScoreboardTeam(player);
            Bukkit.getPluginManager().callEvent(new PostPlayerHideEvent(player, false));
            final ArrayList<Player> vanishedPlayers = new ArrayList<>();
            for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                if (VanishAPI.isInvisible(onlinePlayers))
                    vanishedPlayers.add(onlinePlayers);
            }
            for (final Player vanishedOnlinePlayers : vanishedPlayers) {
                final ScoreboardTeam vanishedOnlinePlayerScoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(vanishedOnlinePlayers.getUniqueId()).get("ScoreboardTeam");
                final ScoreboardTeam.CategorizedScoreboardTeam vanishScoreboardTeam = vanishedOnlinePlayerScoreboardTeam.getCategorizedScoreboardTeam("vanish");
                for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (player != null && VanishAPI.canSee(onlinePlayers, vanishedOnlinePlayers))
                        vanishScoreboardTeam.addViewablePlayer(onlinePlayers);
                }
            }
        }
    }

}
