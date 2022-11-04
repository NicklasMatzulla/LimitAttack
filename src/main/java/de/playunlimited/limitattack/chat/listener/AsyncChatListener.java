package de.playunlimited.limitattack.chat.listener;

import de.playunlimited.limitattack.config.MessagesConfiguration;
import de.playunlimited.limitattack.config.SettingsConfiguration;
import de.playunlimited.limitattack.util.FormatUtil;
import de.playunlimited.limitattack.util.PermissionUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class AsyncChatListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncChatEvent(@NotNull final AsyncChatEvent event) {
        if (event.isCancelled())
            return;
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final String colorChatPermission = SettingsConfiguration.getInstance().FEATURES_CHAT_PERMISSION_COLOR_CHAT;
        final Component messageComponent = event.message();
        final User permissionUser = PermissionUtil.getUserFromPlayer(player);
        final Group permissionGroup = PermissionUtil.getPermissionGroup(permissionUser);
        final ChatColor chatColor = PermissionUtil.getPermissionGroupChatColor(permissionGroup);
        final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacy('&');
        final String prefix = legacyComponentSerializer.serialize(FormatUtil.toComponent(PermissionUtil.getPermissionGroupPrefix(permissionGroup)));
        String message = legacyComponentSerializer.serialize(messageComponent);
        final String chatFormat = ChatColor.translateAlternateColorCodes('&', MessagesConfiguration.getInstance().FEATURES_CHAT_FORMAT
                .replace("<chatColor>", chatColor.toString())
                .replace("<prefix>", prefix)
                .replace("<player>", player.getName()))
                .replace("<message>", player.hasPermission(colorChatPermission) ? ChatColor.translateAlternateColorCodes('&', message) : message);
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
            onlinePlayer.sendMessage(chatFormat);
    }

}
