package de.playunlimited.limitattack.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.Lists;
import de.playunlimited.limitattack.LimitAttack;
import de.playunlimited.limitattack.util.FormatUtil;
import de.playunlimited.limitattack.util.PermissionUtil;
import lombok.SneakyThrows;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ScoreboardTeam {
    private static final ProtocolManager PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();
    private final HashMap<String, CategorizedScoreboardTeam> scoreboardTeams = new HashMap<>();
    private final Player player;

    public ScoreboardTeam(@NotNull final Player player) {
        this.player = player;
        LimitAttack.getCacheProvider().getUserCache(player.getUniqueId()).set("ScoreboardTeam", this);
        createDefaultScoreboardTeam();
    }

    public void unregisterViewerScoreboardTeam(@NotNull final Player viewer) {
        final UUID uniqueId = viewer.getUniqueId();
        for (final CategorizedScoreboardTeam scoreboardTeams : this.scoreboardTeams.values()) {
            if (scoreboardTeams.viewablePlayers.contains(uniqueId))
                scoreboardTeams.removeViewablePlayer(viewer);
        }
    }

    public void setViewerPrimaryScoreboardTeam(@NotNull final Player viewer, @NotNull final String key) {
        final UUID uniqueId = viewer.getUniqueId();
        for (final CategorizedScoreboardTeam scoreboardTeams : this.scoreboardTeams.values()) {
            if (scoreboardTeams.viewablePlayers.contains(uniqueId))
                scoreboardTeams.removeViewablePlayer(viewer);
        }
        final CategorizedScoreboardTeam scoreboardTeam = this.scoreboardTeams.getOrDefault(key, this.scoreboardTeams.get("default"));
        scoreboardTeam.addViewablePlayer(viewer);
    }

    public @Nullable CategorizedScoreboardTeam getCategorizedScoreboardTeam(@NotNull final String key) {
        return this.scoreboardTeams.get(key);
    }

    private void createDefaultScoreboardTeam() {
        final User permissionUser = PermissionUtil.getUserFromPlayer(this.player);
        final Group permissionGroup = PermissionUtil.getPermissionGroup(permissionUser);
        final int permissionGroupWeight = PermissionUtil.getPermissionGroupWeight(permissionGroup);
        final String permissionGroupPrefix = PermissionUtil.getPermissionGroupPrefix(permissionGroup);
        final String scoreboardTeamName = getScoreboardTeamName(permissionGroupWeight, this.player.getName());
        final String scoreboardTeamPrefix = FormatUtil.toJson(permissionGroupPrefix);
        final ChatColor scoreboardTeamChatColor = PermissionUtil.getPermissionGroupChatColor(permissionGroup);
        createSpecificScoreboardTeam("default", scoreboardTeamName, scoreboardTeamChatColor, scoreboardTeamPrefix, null);
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public @NotNull CategorizedScoreboardTeam createSpecificScoreboardTeam(@NotNull final String scoreboardTeamKey, @NotNull final String scoreboardTeamName, @Nullable ChatColor chatColor, @Nullable String jsonPrefix, @Nullable String jsonSuffix) {
        if (chatColor == null || jsonPrefix == null) {
            final User permissionUser = PermissionUtil.getUserFromPlayer(this.player);
            final Group permissionGroup = PermissionUtil.getPermissionGroup(permissionUser);
            if (chatColor == null)
                chatColor = PermissionUtil.getPermissionGroupChatColor(permissionGroup);
            if (jsonPrefix == null)
                jsonPrefix = FormatUtil.toJson(PermissionUtil.getPermissionGroupPrefix(permissionGroup));
        }
        final CategorizedScoreboardTeam categorizedScoreboardTeam = new CategorizedScoreboardTeam(this.player, scoreboardTeamName, chatColor, jsonPrefix, jsonSuffix);
        this.scoreboardTeams.put(scoreboardTeamKey, categorizedScoreboardTeam);
        return categorizedScoreboardTeam;
    }

    private @NotNull String getScoreboardTeamName(int permissionGroupWeight, @NotNull final String playerName) {
        String scoreboardTeamName = (100-permissionGroupWeight) + playerName;
        return scoreboardTeamName.length() > 16 ? scoreboardTeamName.substring(0, 15) : scoreboardTeamName;
    }

    public static class CategorizedScoreboardTeam {
        private final Player player;
        private final String scoreboardTeamName;
        private final ChatColor scoreboardTeamChatColor;
        private final String scoreboardTeamPrefix;
        private final String scoreboardTeamSuffix;
        private final ArrayList<UUID> viewablePlayers = new ArrayList<>();

        public CategorizedScoreboardTeam(@NotNull final Player player, @NotNull final String scoreboardTeamName, @NotNull final ChatColor chatColor, @NotNull final String prefix, @Nullable final String suffix) {
            this.player = player;
            this.scoreboardTeamName = scoreboardTeamName;
            this.scoreboardTeamChatColor = chatColor;
            this.scoreboardTeamPrefix = prefix;
            this.scoreboardTeamSuffix = suffix;
        }

        public void addViewablePlayer(@NotNull final Player viewer) {
            if (!this.viewablePlayers.contains(viewer.getUniqueId()))
                this.viewablePlayers.add(viewer.getUniqueId());
            sendCreateTeamPacket(viewer);
        }

        public void removeViewablePlayer(@NotNull final Player viewer) {
            if (this.viewablePlayers.contains(viewer.getUniqueId()))
                this.viewablePlayers.remove(viewer.getUniqueId());
            sendDeleteTeamPacket(viewer);
        }

        @SneakyThrows
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        private void sendCreateTeamPacket(@NotNull final Player receiver) {
            final PacketContainer packet = ScoreboardTeam.PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
            packet.getStrings().write(0, this.scoreboardTeamName);
            packet.getIntegers().write(0, 0);
            final InternalStructure internalStructure = packet.getOptionalStructures().read(0).get();
            internalStructure.getChatComponents().write(0, WrappedChatComponent.fromJson(this.scoreboardTeamPrefix));
            internalStructure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat"))
                    .write(0, this.scoreboardTeamChatColor);
            internalStructure.getChatComponents().write(1, WrappedChatComponent.fromJson(this.scoreboardTeamPrefix));
            if (this.scoreboardTeamSuffix != null)
                internalStructure.getChatComponents().write(2, WrappedChatComponent.fromJson(this.scoreboardTeamSuffix));
            packet.getOptionalStructures().write(0, Optional.of(internalStructure));
            packet.getModifier().write(2, Lists.newArrayList(this.player.getName()));
            ScoreboardTeam.PROTOCOL_MANAGER.sendServerPacket(receiver, packet);
        }

        @SneakyThrows
        private void sendDeleteTeamPacket(@NotNull final Player receiver) {
            final PacketContainer packet = ScoreboardTeam.PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
            packet.getStrings().write(0, this.scoreboardTeamName);
            packet.getIntegers().write(0, 1);
            ScoreboardTeam.PROTOCOL_MANAGER.sendServerPacket(receiver, packet);
        }
    }
}
