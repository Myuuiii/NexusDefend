package com.myuuiii.nexusdefend;

import com.myuuiii.nexusdefend.entities.NexusLocation;
import com.myuuiii.nexusdefend.enums.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameMap {
    public String Id;
    public Location LobbySpawn;
    public Location DefenderSpawn;
    public Location AttackerSpawn;
    public ArrayList<NexusLocation> NexusLocations;
    public GameState State;
    public List<UUID> Players;

    public GameMap() {
    }

    public GameMap(String id, Location lobbySpawn, Location defenderSpawn, Location attackerSpawn, ArrayList<NexusLocation> nexusLocations) {
        Id = id;
        LobbySpawn = lobbySpawn;
        DefenderSpawn = defenderSpawn;
        AttackerSpawn = attackerSpawn;
        State = GameState.WaitingForPlayers;
        Players = new ArrayList<>();
        NexusLocations = nexusLocations;
    }

    public String getId() {
        return this.Id;
    }

    public GameState getState() {
        return this.State;
    }

    public List<UUID> getPlayers() {
        return this.Players;
    }

    public boolean addPlayer(Player player) {
        if (!this.Players.contains(player.getUniqueId()))
        {
            this.Players.add(player.getUniqueId());
            return true;
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        if (this.Players.contains(player.getUniqueId()))
        {
            this.Players.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    public void teleportToLobby(Player player) {
        player.teleport(this.LobbySpawn);
    }

    public void teleportToDefenderSpawn(Player player) {
        player.teleport(this.DefenderSpawn);
    }

    public void teleportToAttackerSpawn(Player player) {
        player.teleport(this.AttackerSpawn);
    }
}
