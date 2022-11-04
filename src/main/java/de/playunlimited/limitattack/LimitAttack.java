package de.playunlimited.limitattack;

import de.playunlimited.limitattack.chat.listener.*;
import de.playunlimited.limitattack.command.DebugCommand;
import de.playunlimited.limitattack.command.GameModeCommand;
import de.playunlimited.limitattack.config.MessagesConfiguration;
import de.playunlimited.limitattack.config.SettingsConfiguration;
import de.playunlimited.limitcore.CacheProvider;
import de.playunlimited.limitcore.command.Command;
import de.playunlimited.limitcore.command.CommandRegister;
import de.playunlimited.limitcore.config.CoreMessagesConfiguration;
import de.playunlimited.limitcore.config.CoreSettingsConfiguration;
import de.playunlimited.limitcore.config.util.JsonConfigurationType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;

public class LimitAttack extends JavaPlugin {
    @Getter private static LimitAttack instance;
    @Getter private static CacheProvider cacheProvider;

    @Override
    public void onEnable() {
        LimitAttack.instance = this;
        registerLimitCore();

        new SettingsConfiguration(JsonConfigurationType.SETTINGS, new File("plugins/PlayUnlimited.DE/LimitAttack/settings.json"), "configurations/LimitAttack/settings.json");
        new MessagesConfiguration(JsonConfigurationType.MESSAGES, new File("plugins/PlayUnlimited.DE/LimitAttack/messages.json"), "configurations/LimitAttack/messages.json");

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new AsyncChatListener(), this);
        pluginManager.registerEvents(new PostPlayerVanishListener(), this);
        pluginManager.registerEvents(new PlayerChangedWorldListener(), this);

        new GameModeCommand();
        new DebugCommand();
    }

    private void registerLimitCore() {
        LimitAttack.cacheProvider = new CacheProvider();
        new CoreSettingsConfiguration(JsonConfigurationType.SYSTEM, new File("plugins/PlayUnlimited.DE/LimitCore/settings.json"), "configurations/LimitCore/settings.json");
        new CoreMessagesConfiguration(JsonConfigurationType.SYSTEM, new File("plugins/PlayUnlimited.DE/LimitCore/messages.json"), "configurations/LimitCore/messages.json");

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new de.playunlimited.limitcore.listener.PlayerQuitListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SuppressWarnings("ConstantConditions")
    public void addCommand(@NotNull final Command... commands) {
        CommandMap map = null;
        Field field;
        try {
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Command command : commands) {
            if (map.getCommand(command.getName()) == null)
                map.register(getName(), new CommandRegister(command, this));
        }
    }
}