package de.playunlimited.limitattack.chat.listener;

import de.myzelyam.api.vanish.PostPlayerHideEvent;
import de.myzelyam.api.vanish.PostPlayerShowEvent;
import de.myzelyam.api.vanish.VanishAPI;
import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.chat.ScoreboardTeam;
import de.playunlimited.limitattack.chat.TablistStyle;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PostPlayerVanishListener implements Listener {
    @Getter private static PostPlayerVanishListener instance;

    public PostPlayerVanishListener() {
        PostPlayerVanishListener.instance = this;
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPostPlayerHideEvent(@NotNull final PostPlayerHideEvent event) {
        final Player player = event.getPlayer();
        final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(player.getUniqueId()).get("ScoreboardTeam");
        ScoreboardTeam.CategorizedScoreboardTeam vanishScoreboardTeam = scoreboardTeam.getVanishScoreboardTeam();
        for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            scoreboardTeam.unregisterViewerScoreboardTeam(onlinePlayers);
            if (VanishAPI.canSee(onlinePlayers, player))
                vanishScoreboardTeam.addViewablePlayer(onlinePlayers);
        }
        TablistStyle.updateTablist();
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPostPlayerShowEvent(@NotNull final PostPlayerShowEvent event) {
        final Player player = event.getPlayer();
        final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(player.getUniqueId()).get("ScoreboardTeam");
        final ScoreboardTeam.CategorizedScoreboardTeam defaultScoreboardTeam = scoreboardTeam.getDefaultScoreboardTeam();
        for (final Player onlinePlayers : Bukkit.getOnlinePlayers())
            defaultScoreboardTeam.addViewablePlayer(onlinePlayers);
        TablistStyle.updateTablist();
    }
}
