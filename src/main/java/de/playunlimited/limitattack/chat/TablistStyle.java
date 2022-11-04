package de.playunlimited.limitattack.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.playunlimited.limitattack.chat.listener.PostPlayerVanishListener;
import de.playunlimited.limitattack.config.MessagesConfiguration;
import de.playunlimited.limitattack.util.PermissionUtil;
import lombok.SneakyThrows;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TablistStyle {
    private static final ProtocolManager PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();

    public static void updateTablist() {
        final MessagesConfiguration messagesConfiguration = MessagesConfiguration.getInstance();
        final String header = messagesConfiguration.FEATURES_TABLIST_HEADER;
        final String footer = messagesConfiguration.FEATURES_TABLIST_FOOTER;
        for (final Player onlinePlayers : Bukkit.getOnlinePlayers())
            sendTablistPacket(onlinePlayers, header, footer);
    }

    @SneakyThrows
    private static void sendTablistPacket(@NotNull final Player player, @NotNull final String header, @NotNull final String footer) {
        final User permissionUser = PermissionUtil.getUserFromPlayer(player);
        final Group permissionGroup = PermissionUtil.getPermissionGroup(permissionUser);
        final String permissionGroupName = PermissionUtil.getPermissionGroupName(permissionGroup);
        final ChatColor permissionGroupChatColor = PermissionUtil.getPermissionGroupChatColor(permissionGroup);
        final WrappedChatComponent headerComponent = WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', header
                .replace("<player>", player.getName())));
        final WrappedChatComponent footerComponent = WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', footer
                .replace("<server>", "LimitAttack")
                .replace("<playerCount>", String.valueOf(PostPlayerVanishListener.nonVanishedPlayerCount))
                .replace("<maxPlayerCount>", String.valueOf(Bukkit.getServer().getMaxPlayers()))
                .replace("<permissionGroupChatColor>", permissionGroupChatColor.toString()))
                .replace("<permissionGroupName>", permissionGroupName));
        final PacketContainer packet = TablistStyle.PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        packet.getChatComponents().write(0, headerComponent);
        packet.getChatComponents().write(1, footerComponent);
        TablistStyle.PROTOCOL_MANAGER.sendServerPacket(player, packet);
    }
}
