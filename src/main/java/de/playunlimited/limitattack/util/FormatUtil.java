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
}
