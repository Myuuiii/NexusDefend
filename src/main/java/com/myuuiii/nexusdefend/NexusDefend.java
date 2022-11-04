package com.myuuiii.nexusdefend;

import com.myuuiii.nexusdefend.commands.BaseCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class NexusDefend extends JavaPlugin {

    ConfigManager _data;
    NexusDefend _plugin;

    @Override
    public void onEnable() {
        this._plugin = this;
        ConfigManager.setupConfig(this);
        _data = new ConfigManager();

        getCommand("nd").setExecutor(new BaseCommand(_plugin));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public NexusDefend getPlugin() {
        return this._plugin;
    }

    public ConfigManager getData() {
        return this._data;
    }
}
