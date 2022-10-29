package de.playunlimited.limitcore.config.util;

import com.google.gson.*;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonConfiguration {
    @Getter private static JsonConfiguration confidentialConfiguration;
    @Getter private static JsonConfiguration settingsConfiguration;
    @Getter private static JsonConfiguration messagesConfiguration;
    @Getter public static JsonConfiguration coreConfidentialConfiguration;
    @Getter public static JsonConfiguration coreSettingsConfiguration;
    @Getter public static JsonConfiguration coreMessagesConfiguration;

    private final JsonConfigurationType configurationType;
    private final File configurationFile;
    private final String classpathFile;
    private JsonObject configuration;

    public JsonConfiguration(@NotNull final JsonConfigurationType configurationType, @NotNull final File configurationFile, @NotNull final String classpathFile) {
        this.configurationType = configurationType;
        this.configurationFile = configurationFile;
        this.classpathFile = classpathFile;
        reload();
    }

    public final void reload() {
        try {
            if (!this.configurationFile.exists()) {
                final InputStream classpathConfigurationInputStream = getClass().getClassLoader().getResourceAsStream(this.classpathFile);
                if (classpathConfigurationInputStream != null)
                    FileUtils.copyInputStreamToFile(classpathConfigurationInputStream, this.configurationFile);
            }
            final InputStream configurationInputStream = new FileInputStream(this.configurationFile);
            this.configuration = JsonParser.parseReader(new InputStreamReader(configurationInputStream)).getAsJsonObject();
            switch (this.configurationType) {
                case CONFIDENTIAL -> JsonConfiguration.confidentialConfiguration = this;
                case SETTINGS -> JsonConfiguration.settingsConfiguration = this;
                case MESSAGES -> JsonConfiguration.messagesConfiguration = this;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private @NotNull JsonElement getJsonElement(@NotNull final String path) {
        final String[] parts = path.split("[.\\[\\]]");
        JsonElement result = this.configuration;
        for (@NotNull String key : parts) {
            key = key.trim();
            if (key.isEmpty())
                continue;
            if (result == null) {
                result = JsonNull.INSTANCE;
                break;
            }
            if (result.isJsonObject())
                result = ((JsonObject) result).get(key);
            else if (result.isJsonArray()) {
                int ix = Integer.parseInt(key) - 1;
                result = ((JsonArray) result).get(ix);
            }
            else break;
        }
        return result;
    }

    public final @Nullable Byte getByte(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber() ? result.getAsByte() : null;
    }

    public final @Nullable Short getShort(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber() ? result.getAsShort() : null;
    }

    public final @Nullable Integer getInteger(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber() ? result.getAsInt() : null;
    }

    public final @Nullable Long getLong(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber() ? result.getAsLong() : null;
    }

    public final @Nullable Character getCharacter(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isString() ? result.getAsString().charAt(0) : null;
    }

    public final @Nullable Float getFloat(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber() ? result.getAsFloat() : null;
    }

    public final @Nullable Double getDouble(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber() ? result.getAsDouble() : null;
    }

    public final @Nullable Boolean getBoolean(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isBoolean() ? result.getAsBoolean() : null;
    }

    public final @Nullable String getString(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        return result.isJsonPrimitive() && result.getAsJsonPrimitive().isString() ? result.getAsString() : null;
    }

    public final @Nullable Set<String> getStringSet(@NotNull final String path) {
        final JsonElement result = getJsonElement(path);
        if (result.isJsonArray()) {
            final Set<String> resultSet = new HashSet<>();
            for (final JsonElement element : result.getAsJsonArray())
                resultSet.add(element.getAsString());
            return resultSet;
        }
        return null;
    }
}
