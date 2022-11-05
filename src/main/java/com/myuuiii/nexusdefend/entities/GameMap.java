package com.myuuiii.nexusdefend.entities;

import com.google.common.collect.TreeMultimap;
import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.kits.*;
import com.myuuiii.nexusdefend.enums.GameState;
import com.myuuiii.nexusdefend.enums.KitType;
import com.myuuiii.nexusdefend.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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
    private GameTimer timer;
    private HashMap<UUID, Team> teams;
    private HashMap<UUID, Kit> kits;

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
            if (getTeam(player) == Team.ATTACKERS)
                setKit(player.getUniqueId(), KitType.WARRIOR);
            else setKit(player.getUniqueId(), KitType.DEFENDER);

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
            removeTeam(player);
            removeKit(player.getUniqueId());
            if (this.State.equals(GameState.Countdown) && Players.size() < ConfigManager.getMinimumPlayers(this.getId())) {
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
            teams.clear();
            for (UUID uuid : Players) {
                Player player = Bukkit.getPlayer(uuid);
                player.getInventory().clear();

                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potionEffect.getType());
                }

                removeKit(uuid);
            }
        } catch (Exception e) {
            // haha. no
        }

        timer = new GameTimer(plugin, this);
        countdown = new Countdown(plugin, this);
        gameInfo = new GameInfo(this);
        if (backToLobby)
            for (UUID uuid : Players) {
                teleportToLobby(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
            }
        Players.clear();
        teams.clear();
    }

    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    public void endGame(boolean allNexusesDestroyed, boolean defendedForTime) {
        if (allNexusesDestroyed) {
            this.sendMessage("All the nexuses have been destroyed! Attackers win!");
            this.reset(true);
        } else if (defendedForTime) {
            this.sendMessage("The defenders have defended the nexuses successfully! Defenders win!");
            this.reset(true);
        } else {
            this.sendMessage("Your game has been stopped by an administrator");
            this.reset(true);
        }
    }

    /* Team Management */
    public void setTeam(Player player, Team team) {
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
        player.sendMessage("You have been placed on the " + team.getDisplay() + ChatColor.RESET + " team");
        if (team == Team.DEFENDERS) {
            setKit(player.getUniqueId(), KitType.DEFENDER);
        }
    }

    public void removeTeam(Player player) {
        if (teams.containsKey(player.getUniqueId())) {
            teams.remove(player.getUniqueId());
        }
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
        for (KitType t : KitType.values()) {
            if (t == kit) {
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
}
