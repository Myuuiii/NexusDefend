package com.myuuiii.nexusdefend;

import com.myuuiii.nexusdefend.commands.BaseCommand;
import com.myuuiii.nexusdefend.commands.BaseCommandCompleter;
import com.myuuiii.nexusdefend.commands.NdCommand;
import com.myuuiii.nexusdefend.commands.NdCommandCompleter;
import com.myuuiii.nexusdefend.listeners.ConnectListener;
import com.myuuiii.nexusdefend.listeners.GameListener;
import com.myuuiii.nexusdefend.listeners.PlayerRespawnListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NexusDefend extends JavaPlugin {

    ConfigManager _data;
    NexusDefend _plugin;
    GameMapManager _manager;

    @Override
    public void onEnable() {
        this._plugin = this;
        ConfigManager.setupConfig(this);
        _data = new ConfigManager();
        _manager = new GameMapManager(this);

        getCommand("nda").setExecutor(new BaseCommand(_plugin));
        getCommand("nda").setTabCompleter(new BaseCommandCompleter(_plugin));

        getCommand("nd").setExecutor(new NdCommand(_plugin));
        getCommand("nd").setTabCompleter(new NdCommandCompleter(_plugin));

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(this, this._manager), this);
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

    public GameMapManager getMapManager() {
        return this._manager;
    }
}
