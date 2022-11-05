package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;
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
    }

    public void damageNexus(Player player, String nexusId) {
        int currentHealth = NexusHealth.get(nexusId);
        if (currentHealth == 1) {
            _map.sendMessage(ChatColor.RED + player.getName() + ChatColor.RESET + " has " + ChatColor.RED + ChatColor.BOLD + "destroyed" + ChatColor.RESET + " nexus " + ChatColor.GOLD + nexusId);
            currentHealth--;
            return;
        }
        if (currentHealth == 0) {
            return;
        }
        currentHealth--;
        NexusHealth.replace(nexusId, currentHealth);
        _map.sendMessage(ChatColor.RED + player.getName() + ChatColor.RESET + " has damaged nexus " + ChatColor.GOLD + nexusId);
    }

    public int getNexusHealth(String nexusId) {
        return NexusHealth.get(nexusId);
    }
}
