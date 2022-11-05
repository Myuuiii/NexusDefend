package com.myuuiii.nexusdefend.listeners;

import com.myuuiii.nexusdefend.GameMapManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.entities.RespawnTimer;
import com.myuuiii.nexusdefend.enums.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    private NexusDefend plugin;
    private GameMapManager _mapManager;

    public PlayerRespawnListener(NexusDefend plugin, GameMapManager _mapManager) {
        this.plugin = plugin;
        this._mapManager = _mapManager;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = (Player)event.getPlayer();
        GameMap map = _mapManager.getMap(player);
        player.getInventory().clear();
        event.setRespawnLocation(map.LobbySpawn);
        player.setInvulnerable(true);
        new RespawnTimer(plugin, map, map.getTeam(player) == Team.ATTACKERS, player);
    }
}
