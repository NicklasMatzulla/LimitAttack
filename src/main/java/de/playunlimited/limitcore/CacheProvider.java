package de.playunlimited.limitcore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class CacheProvider {
    private final HashMap<UUID, UserCache> userCaches = new HashMap<>();

    public @NotNull UserCache getUserCache(@NotNull final UUID uniqueId) {
        if (!this.userCaches.containsKey(uniqueId))
            this.userCaches.put(uniqueId, new UserCache());
        return this.userCaches.get(uniqueId);
    }

    public void deleteUserCache(@NotNull final UUID uniqueId) {
        this.userCaches.remove(uniqueId);
    }

    public static class UserCache {
        private final HashMap<Object, Object> userCache = new HashMap<>();

        public @Nullable Object get(@NotNull final Object key) {
            return this.userCache.get(key);
        }

        public @Nullable Object set(@NotNull final Object key, @NotNull final Object value) {
            return this.userCache.put(key, value);
        }

        public @Nullable Object remove(@NotNull final Object key) {
            return this.userCache.remove(key);
        }
    }

}
