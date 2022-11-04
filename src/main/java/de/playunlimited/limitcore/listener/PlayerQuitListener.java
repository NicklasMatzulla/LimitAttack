package de.playunlimited.limitcore.listener;

import de.playunlimited.limitattack.LimitAttack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(@NotNull final PlayerQuitEvent event) {
        new Thread(() -> {
            try {
                Thread.sleep(5);
                final Player player = event.getPlayer();
                LimitAttack.getCacheProvider().deleteUserCache(player.getUniqueId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
