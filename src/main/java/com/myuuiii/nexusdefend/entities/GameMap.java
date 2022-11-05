package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameMap {
    public String Id;
    public Location LobbySpawn;
    public Location DefenderSpawn;
    public Location AttackerSpawn;
    public ArrayList<NexusLocation> NexusLocations;
    public GameState State;
    public List<UUID> Players;
    private Countdown countdown;
    private NexusDefend plugin;
    private GameInfo gameInfo;
    private GameTimer timer;

    public GameMap() {
    }

    public GameMap(NexusDefend plugin, String id, Location lobbySpawn, Location defenderSpawn, Location attackerSpawn, ArrayList<NexusLocation> nexusLocations) {
        this.plugin = plugin;
        this.Id = id;
        this.LobbySpawn = lobbySpawn;
        this.DefenderSpawn = defenderSpawn;
        this.AttackerSpawn = attackerSpawn;
        this.State = GameState.WaitingForPlayers;
        this.Players = new ArrayList<>();
        this.NexusLocations = nexusLocations;
        this.countdown = new Countdown(this.plugin, this);
        this.timer = new GameTimer(this.plugin, this);
        this.gameInfo = new GameInfo(this);
    }

    public String getId() {
        return this.Id;
    }

    public GameState getState() {
        return this.State;
    }

    public void setState(GameState state) {
        this.State = state;
    }

    public List<UUID> getPlayers() {
        return this.Players;
    }

    public boolean addPlayer(Player player) {
        if (!this.Players.contains(player.getUniqueId())) {
            this.Players.add(player.getUniqueId());
            if (this.State.equals(GameState.WaitingForPlayers) && Players.size() >= ConfigManager.getMinimumPlayers(this.getId())) {
                countdown.start();
            }
            return true;
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        if (this.Players.contains(player.getUniqueId())) {
            this.Players.remove(player.getUniqueId());
            if (this.State.equals(GameState.Countdown) && Players.size() < ConfigManager.getMinimumPlayers(this.getId()))
            {
                reset(false);
            }
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

    public void sendMessage(String message) {
        for (UUID uuid : getPlayers()) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle) {
        for (UUID uuid : getPlayers()) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
        }
    }

    public void start() {
        gameInfo.start();
        timer.start();
    }

    public void reset(boolean backToLobby) {
        setState(GameState.WaitingForPlayers);
        try {
            countdown.cancel();
            timer.cancel();
        }
        catch (Exception e) {
            // haha. no
        }

        timer = new GameTimer(plugin, this);
        countdown = new Countdown(plugin, this);
        gameInfo = new GameInfo(this);
        if (backToLobby)
            for (UUID uuid : Players) {
                teleportToLobby(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
            }
    }

    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    public void endGame() {
        reset(true);
        sendMessage("Thank you for playing");
        Players.clear();
    }
}
