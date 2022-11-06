package com.myuuiii.nexusdefend.entities;

import com.google.common.collect.TreeMultimap;
import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.kits.*;
import com.myuuiii.nexusdefend.enums.GameState;
import com.myuuiii.nexusdefend.enums.KitType;
import com.myuuiii.nexusdefend.enums.Team;
import com.myuuiii.nexusdefend.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

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
    public GameTimer timer;
    private HashMap<UUID, Team> teams;
    private HashMap<UUID, Kit> kits;
    public HashMap<UUID, ScoreboardManager> scoreboards;
    private ScoreboardManager scoreboardManager;

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
        this.teams = new HashMap<>();
        this.kits = new HashMap<>();
        this.scoreboards = new HashMap<>();
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

            // - BALANCING TEAMS
            TreeMultimap<Integer, Team> count = TreeMultimap.create();
            for (Team team : Team.values()) {
                count.put(getTeamCount(team), team);
            }
            Team lowest = (Team) count.values().toArray()[0];
            setTeam(player, lowest);
            // - END BALANCING

            this.Players.add(player.getUniqueId());

            // Add player to scoreboard
            addScoreboard(player);

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
            removeTeam(player);
            removeKit(player.getUniqueId());
            removeScoreboard(player);
            if (this.State.equals(GameState.Countdown) && Players.size() < ConfigManager.getMinimumPlayers(this.getId())) {
                this.State = GameState.WaitingForPlayers;
                this.sendMessage("Too few players to start the game, going back into recruiting state");
                reset(false, false);
            }
            if (this.State.equals(GameState.Live) && Players.size() == 0) {
                this.reset(true, true);
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

    public void reset(boolean backToLobby, boolean removeAllPlayersFromGame) {
        setState(GameState.WaitingForPlayers);
        try {
            countdown.cancel();
            timer.cancel();
        } catch (Exception e) {
            // haha. no
        }
        teams.clear();
        for (UUID uuid : Players) {
            Player player = Bukkit.getPlayer(uuid);
            player.getInventory().clear();

            removeScoreboard(player);

            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            removeKit(uuid);
        }
        timer = new GameTimer(plugin, this);
        countdown = new Countdown(plugin, this);
        gameInfo = new GameInfo(this);
        if (backToLobby)
            for (UUID uuid : Players) {
                teleportToLobby(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
            }
        if (removeAllPlayersFromGame) {
            Players.clear();
            teams.clear();
        }
    }

    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    public void endGame(boolean allNexusesDestroyed, boolean defendedForTime) {
        if (allNexusesDestroyed) {
            this.sendMessage("All the nexuses have been destroyed! Attackers win!");
            this.reset(true, true);
        } else if (defendedForTime) {
            this.sendMessage("The defenders have defended the nexuses successfully! Defenders win!");
            this.reset(true, true);
        } else {
            this.sendMessage("Your game has been stopped by an administrator");
            this.reset(true, true);
        }
    }

    /* Team Management */
    public void setTeam(Player player, Team team) {
        removeTeam(player);
        if (teams.containsKey(player.getUniqueId())) {
            teams.replace(player.getUniqueId(), team);
        } else {
            teams.put(player.getUniqueId(), team);
        }
        player.sendMessage("You have been placed on the " + team.getDisplay() + ChatColor.RESET + " team");
        if (team == Team.DEFENDERS) {
            setKit(player.getUniqueId(), KitType.DEFENDER);
        }
        if (team == Team.ATTACKERS) {
            setKit(player.getUniqueId(), KitType.WARRIOR);
        }
    }

    private KitType getKit(Player player) {
        if (kits.containsKey(player.getUniqueId()))
            return kits.get(player.getUniqueId()).getType();
        return null;
    }

    public void removeTeam(Player player) {
        teams.remove(player.getUniqueId());
    }

    public int getTeamCount(Team team) {
        int amount = 0;
        for (Team t : teams.values()) {
            if (t == team) {
                amount++;
            }
        }
        return amount;
    }

    public Team getTeam(Player player) {
        return teams.get(player.getUniqueId());
    }

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);
        switch (type) {
            case WARRIOR:
                kits.put(uuid, new WarriorKit(plugin, uuid));
                break;
            case ARCHER:
                kits.put(uuid, new ArcherKit(plugin, uuid));
                break;
            case HEALER:
                kits.put(uuid, new HealerKit(plugin, uuid));
                break;
            case DESTROYER:
                kits.put(uuid, new DestroyerKit(plugin, uuid));
                break;
            case DEFENDER:
                kits.put(uuid, new DefenderKit(plugin, uuid));
                break;
        }
        Bukkit.getPlayer(uuid).sendMessage("You have selected the " + type.getDisplay() + ChatColor.RESET + " kit");
    }

    public void removeKit(UUID uuid) {
        if (kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }

    public HashMap<UUID, Kit> getKits() {
        return this.kits;
    }

    public int getKitCount(KitType kit) {
        int amount = 0;
        for (Kit t : kits.values()) {
            if (t.getType() == kit) {
                amount++;
            }
        }
        return amount;
    }

    public KitType getKitType(Player player) {
        if (kits.containsKey(player.getUniqueId()))
            return kits.get(player.getUniqueId()).getType();
        return null;
    }

    public void respawnPlayer(Player player) {
        player.setInvulnerable(false);
        teleportPlayerToTeamSpawn(player);
    }

    public void teleportPlayerToTeamSpawn(Player player) {
        switch (getTeam(player)) {
            case ATTACKERS:
                teleportToAttackerSpawn(player);
                break;
            case DEFENDERS:
                teleportToDefenderSpawn(player);
                break;
        }
        kits.get(player.getUniqueId()).onStart(player);
    }

    public void addScoreboard(Player player){
        if (scoreboards.containsKey(player.getUniqueId())) return;
        scoreboards.put(player.getUniqueId(), new ScoreboardManager(this, player));
    }

    public void removeScoreboard(Player player) {
        if (!scoreboards.containsKey(player.getUniqueId())) return;
        ScoreboardManager manager = scoreboards.get(player.getUniqueId());
        manager.dispose();
        scoreboards.remove(player.getUniqueId());
    }
}
