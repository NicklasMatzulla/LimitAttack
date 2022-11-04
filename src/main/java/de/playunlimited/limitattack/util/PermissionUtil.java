package de.playunlimited.limitattack.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class PermissionUtil {
    public static LuckPerms luckPerms = LuckPermsProvider.get();

    @SuppressWarnings("ConstantConditions")
    public static @NotNull User getUserFromPlayer(@NotNull final Player player) {
        return getUserFromUniqueId(player.getUniqueId());
    }

    public static @Nullable User getUserFromPlayerName(@NotNull final String playerName) {
        final UserManager userManager = PermissionUtil.luckPerms.getUserManager();
        return userManager.getUser(playerName);
    }

    public static @Nullable User getUserFromUniqueId(@NotNull final UUID uniqueId) {
        final UserManager userManager = PermissionUtil.luckPerms.getUserManager();
        return userManager.getUser(uniqueId);
    }

    public static @Nullable User getUserFromUniqueId(@NotNull final String uniqueId) {
        final UserManager userManager = PermissionUtil.luckPerms.getUserManager();
        return userManager.getUser(UUID.fromString(uniqueId));
    }

    @SuppressWarnings("ConstantConditions")
    public static @NotNull Group getPermissionGroup(@NotNull final User permissionUser) {
        final GroupManager groupManager = PermissionUtil.luckPerms.getGroupManager();
        final String primaryGroupName = permissionUser.getPrimaryGroup();
        return groupManager.getGroup(primaryGroupName);
    }

    @SuppressWarnings("ConstantConditions")
    public static @NotNull String getPermissionGroupName(@NotNull final Group permissionGroup) {
        return permissionGroup.getDisplayName();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static int getPermissionGroupWeight(@NotNull final Group permissionGroup) {
        return permissionGroup.getWeight().getAsInt();
    }

    @SuppressWarnings("ConstantConditions")
    public static @NotNull String getPermissionGroupPrefix(@NotNull final Group permissionGroup) {
        return permissionGroup.getCachedData().getMetaData().getPrefix();
    }

    public static @NotNull ChatColor getPermissionGroupChatColor(@NotNull final Group permissionGroup) {
        final String textColor = permissionGroup.getCachedData().getMetaData().getMetaValue("chatColor");
        return ChatColor.valueOf(textColor);
    }

    public static @NotNull String getPermissionGroupRawChatColor(@NotNull final Group permissionGroup) {
        return "<" + permissionGroup.getCachedData().getMetaData().getMetaValue("chatColor") + ">";
    }
}
