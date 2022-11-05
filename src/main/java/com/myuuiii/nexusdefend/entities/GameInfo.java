package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GameInfo {
    private GameMap _map;

    private HashMap<String, Integer> NexusHealth = new HashMap<>();

    public GameInfo(GameMap map) {
        this._map = map;
    }

    public void start() {
        this._map.setState(GameState.Live);
        this._map.sendMessage(ChatColor.GREEN + "Game has started");

        for (NexusLocation nexus : _map.NexusLocations) {
            NexusHealth.put(nexus.NexusId, nexus.Health);
        }

        for (UUID uuid : _map.getPlayers()) {
            Bukkit.getPlayer(uuid).closeInventory();
        }
    }

    public void damageNexus(Player player, String nexusId) {
        int currentHealth = NexusHealth.get(nexusId);
        currentHealth--;
        if (currentHealth == 0) {
            _map.sendMessage(ChatColor.RED + player.getName() + ChatColor.RESET + " has " + ChatColor.RED + ChatColor.BOLD + "destroyed" + ChatColor.RESET + " nexus " + ChatColor.GOLD + nexusId);
            NexusHealth.replace(nexusId, currentHealth);
            return;
        }
        if (currentHealth < 0) {
            return;
        }
        NexusHealth.replace(nexusId, currentHealth);
        _map.sendMessage(ChatColor.RED + player.getName() + ChatColor.RESET + " has damaged nexus " + ChatColor.GOLD + nexusId);
    }

    public int getNexusHealth(String nexusId) {
        return NexusHealth.get(nexusId);
    }

    // Returns true when there is still nexuses with health, false if everything is zero
    public void checkNexusStatesAndEndGameIfAllDestroyed() {
        boolean anyNexusAlive = false;
        for (NexusLocation nexus : _map.NexusLocations) {
            if (NexusHealth.get(nexus.NexusId) != 0) anyNexusAlive = true;
        }
        if (!anyNexusAlive) {
            _map.endGame(true, false);
        }
    }
}
