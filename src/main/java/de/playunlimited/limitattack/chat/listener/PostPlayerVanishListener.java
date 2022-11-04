package de.playunlimited.limitattack.chat.listener;

import de.myzelyam.api.vanish.PostPlayerHideEvent;
import de.myzelyam.api.vanish.PostPlayerShowEvent;
import de.myzelyam.api.vanish.VanishAPI;
import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.chat.ScoreboardTeam;
import de.playunlimited.limitattack.chat.TablistStyle;
import de.playunlimited.limitattack.util.FormatUtil;
import de.playunlimited.limitattack.util.PermissionUtil;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PostPlayerVanishListener implements Listener {
    public static int nonVanishedPlayerCount;

    @EventHandler
    public void onPostPlayerHideEvent(@NotNull final PostPlayerHideEvent event) {
        Bukkit.broadcast(Component.text("vanished"));
        final Player player = event.getPlayer();
        final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(player.getUniqueId()).get("ScoreboardTeam");
        ScoreboardTeam.CategorizedScoreboardTeam vanishScoreboardTeam = scoreboardTeam.getCategorizedScoreboardTeam("vanish");
        if (vanishScoreboardTeam == null) {
            final User permissionUser = PermissionUtil.getUserFromPlayer(player);
            final Group permissionGroup = PermissionUtil.getPermissionGroup(permissionUser);
            final String permissionGroupPrefix = PermissionUtil.getPermissionGroupPrefix(permissionGroup);
            final String scoreboardTeamPrefix = FormatUtil.toJson(permissionGroupPrefix);
            final ChatColor scoreboardTeamChatColor = PermissionUtil.getPermissionGroupChatColor(permissionGroup);
            vanishScoreboardTeam = scoreboardTeam.createSpecificScoreboardTeam("vanish", "100vanish", scoreboardTeamChatColor, scoreboardTeamPrefix, FormatUtil.toJson(" <DARK_GRAY>[<YELLOW>VANISH<DARK_GRAY>]"));
        }
        int nonVanishedPlayerCount = 0;
        for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            scoreboardTeam.unregisterViewerScoreboardTeam(onlinePlayers);
            if (!VanishAPI.isInvisible(onlinePlayers))
                nonVanishedPlayerCount++;
        }
        PostPlayerVanishListener.nonVanishedPlayerCount = nonVanishedPlayerCount;
        TablistStyle.updateTablist();
        ScoreboardTeam.CategorizedScoreboardTeam finalVanishScoreboardTeam = vanishScoreboardTeam;
        Bukkit.getScheduler().scheduleSyncDelayedTask(LimitAttack.getInstance(), () -> {
            finalVanishScoreboardTeam.addViewablePlayer(player);
            for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                if (VanishAPI.canSee(player, onlinePlayers))
                    finalVanishScoreboardTeam.addViewablePlayer(onlinePlayers);
            }
        }, 10);
    }

    @EventHandler
    public void onPostPlayerShowEvent(@NotNull final PostPlayerShowEvent event) {
        final Player player = event.getPlayer();
        final ScoreboardTeam scoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(player.getUniqueId()).get("ScoreboardTeam");
        final ScoreboardTeam.CategorizedScoreboardTeam defaultScoreboardTeam = scoreboardTeam.getCategorizedScoreboardTeam("default");
        int nonVanishedPlayerCount = 0;
        for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            scoreboardTeam.unregisterViewerScoreboardTeam(onlinePlayers);
            defaultScoreboardTeam.addViewablePlayer(onlinePlayers);
            if (!VanishAPI.isInvisible(onlinePlayers))
                nonVanishedPlayerCount++;
        }
        PostPlayerVanishListener.nonVanishedPlayerCount = nonVanishedPlayerCount;
        TablistStyle.updateTablist();
        Bukkit.getScheduler().scheduleSyncDelayedTask(LimitAttack.getInstance(), () -> {
            defaultScoreboardTeam.addViewablePlayer(player);
            final ArrayList<Player> vanishedPlayers = new ArrayList<>();
            for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                if (VanishAPI.isInvisible(onlinePlayers))
                    vanishedPlayers.add(onlinePlayers);
            }
            for (final Player vanishedOnlinePlayers : vanishedPlayers) {
                final ScoreboardTeam vanishedOnlinePlayerScoreboardTeam = (ScoreboardTeam) LimitAttack.getCacheProvider().getUserCache(vanishedOnlinePlayers.getUniqueId()).get("ScoreboardTeam");
                final ScoreboardTeam.CategorizedScoreboardTeam vanishScoreboardTeam = vanishedOnlinePlayerScoreboardTeam.getCategorizedScoreboardTeam("vanish");
                for (final Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (VanishAPI.canSee(onlinePlayers, vanishedOnlinePlayers))
                        vanishScoreboardTeam.addViewablePlayer(onlinePlayers);
                }
            }
        }, 1);
        TablistStyle.updateTablist();
    }
}
