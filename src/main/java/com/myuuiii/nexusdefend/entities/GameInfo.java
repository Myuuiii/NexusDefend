package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.enums.GameState;
import com.myuuiii.nexusdefend.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GameInfo {
    private final GameMap _map;

    private final HashMap<String, Integer> NexusHealth = new HashMap<>();

    public GameInfo(GameMap map) {
        this._map = map;
    }

    public void start() {
        this._map.setState(GameState.Live);
        this._map.sendMessage(ChatColor.GREEN + "Game has started");

        for (UUID uuid : _map.getKits().keySet()) {
            _map.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
        }

        for (NexusLocation nexus : _map.NexusLocations) {
            NexusHealth.put(nexus.NexusId, nexus.Health);
        }

        for (UUID uuid : _map.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            player.closeInventory();
            _map.teleportPlayerToTeamSpawn(player);
            player.setInvulnerable(false);
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

        // Update scoreboards
        for (ScoreboardManager scoreboardManager : _map.scoreboards.values()) {
            scoreboardManager.updateScoreboard();
        }
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
