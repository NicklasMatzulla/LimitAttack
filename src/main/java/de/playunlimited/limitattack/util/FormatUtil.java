package de.playunlimited.limitattack.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class FormatUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final GsonComponentSerializer GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.gson();

    public static @NotNull String toJson(@NotNull final String input) {
        return FormatUtil.GSON_COMPONENT_SERIALIZER.serialize(FormatUtil.MINI_MESSAGE.deserialize(input));
    }

    public static @NotNull Component toComponent(@NotNull final String input) {
        return FormatUtil.MINI_MESSAGE.deserialize(input);
    }

    public static @NotNull String replaceChatColors(@NotNull final String input) {
        return input
                .replace("&0", "<BLACK>")
                .replace("&1", "<DARK_BLUE>")
                .replace("&2", "<DARK_GREEN>")
                .replace("&3", "<DARK_AQUA>")
                .replace("&4", "<DARK_RED>")
                .replace("&5", "<DARK_PURPLE>")
                .replace("&6", "<GOLD>")
                .replace("&7", "<GRAY>")
                .replace("&8", "<DARK_GRAY>")
                .replace("&9", "<BLUE>")
                .replace("&a", "<GREEN>")
                .replace("&b", "<AQUA>")
                .replace("&c", "<RED>")
                .replace("&d", "<LIGHT_PURPLE>")
                .replace("&e", "<YELLOW>")
                .replace("&f", "<WHITE>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underline>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset><GRAY>");
    }

}
