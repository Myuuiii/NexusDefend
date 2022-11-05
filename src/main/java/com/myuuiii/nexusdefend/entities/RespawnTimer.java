package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class RespawnTimer extends BukkitRunnable {
    private NexusDefend _plugin;
    private GameMap _map;
    private int respawnTime;
    private boolean isAttacker;
    private Player player;


    public RespawnTimer(NexusDefend _plugin, GameMap _map, boolean isAttacker, Player player) {
        this._plugin = _plugin;
        this._map = _map;
        this.player = player;
        if (isAttacker)
            respawnTime = ConfigManager.getRespawnTimeAttacker(_map.getId());
        else
            respawnTime = ConfigManager.getRespawnTimeDefender(_map.getId());
        runTaskTimer(_plugin, 0, 20);
    }

    @Override
    public void run() {
        if (respawnTime == 0) {
            cancel();
            _map.respawnPlayer(player);
        }

        if (respawnTime <= 10) {
            player.sendMessage("Respawning in " + ChatColor.GREEN + respawnTime);
        }

        respawnTime--;
    }
}
