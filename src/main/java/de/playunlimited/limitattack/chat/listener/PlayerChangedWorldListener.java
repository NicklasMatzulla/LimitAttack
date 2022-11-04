package de.playunlimited.limitattack.chat.listener;

import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.chat.ScoreboardTeam;
import de.playunlimited.limitcore.CacheProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerChangedWorldListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onPlayerChangedWorldEvent(@NotNull final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final CacheProvider.UserCache userCache = LimitAttack.getCacheProvider().getUserCache(player.getUniqueId());
        if (userCache.remove("SetPlayerJoinVanishedScoreboard") != null) {
            final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) userCache.get("ScoreboardTeam");
            final ScoreboardTeam.CategorizedScoreboardTeam vanishScoreboardTeam = scoreboardTeam.getVanishScoreboardTeam();
            vanishScoreboardTeam.addViewablePlayer(player);
        }
    }

}
