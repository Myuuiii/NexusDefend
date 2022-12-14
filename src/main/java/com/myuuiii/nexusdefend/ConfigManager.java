package com.myuuiii.nexusdefend;

import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.entities.NexusLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigManager {
    private static FileConfiguration config;
    private static NexusDefend _plugin;
    private File _file;

    public static void setupConfig(NexusDefend plugin) {
        _plugin = plugin;
        ConfigManager.config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public static int getRespawnTimeAttacker(String id) {
        return config.getInt("maps." + id + ".attackerRespawnTime");
    }

    public static int getRespawnTimeDefender(String id) {
        return config.getInt("maps." + id + ".defenderRespawnTime");
    }

    public static int getMinimumPlayers(String mapId) {
        return config.getInt("maps." + mapId + ".minimumPlayers");
    }

    public static int getGameTime(String mapId) {
        return config.getInt("maps." + mapId + ".gameTime");
    }

    public static int getCountdownTime() {
        return config.getInt("countdown");
    }

    public String getWorldName(String mapId) {
        String mapName = config.getString("maps." + mapId + ".world");
        return mapName;
    }

    public boolean getTeamSelectDisabled(String mapId) {
        return config.getBoolean("maps." + mapId + ".disableTeamSelect");
    }

    public ArrayList<String> getWorldIds() {
        return new ArrayList<>(Objects.requireNonNull(config.getConfigurationSection("maps")).getKeys(false));
    }

    public Location getSpawnLocation(String mapId) {
        Location loc = new Location(
                getWorld(mapId),
                config.getInt("maps." + mapId + ".spawnCoords.x"),
                config.getInt("maps." + mapId + ".spawnCoords.y"),
                config.getInt("maps." + mapId + ".spawnCoords.z"),
                (float) config.getDouble("maps." + mapId + ".spawnCoords.pitch"),
                (float) config.getDouble("maps." + mapId + ".spawnCoords.yaw")
        );
        return loc;
    }

    public Location getAttackerSpawnLocation(String mapId) {
        Location loc = new Location(
                getWorld(mapId),
                config.getInt("maps." + mapId + ".attackerCoords.x"),
                config.getInt("maps." + mapId + ".attackerCoords.y"),
                config.getInt("maps." + mapId + ".attackerCoords.z"),
                (float) config.getDouble("maps." + mapId + ".attackerCoords.pitch"),
                (float) config.getDouble("maps." + mapId + ".attackerCoords.yaw")
        );
        return loc;
    }

    public Location getDefenderSpawnLocation(String mapId) {
        Location loc = new Location(
                getWorld(mapId),
                config.getInt("maps." + mapId + ".defenderCoords.x"),
                config.getInt("maps." + mapId + ".defenderCoords.y"),
                config.getInt("maps." + mapId + ".defenderCoords.z"),
                (float) config.getDouble("maps." + mapId + ".defenderCoords.pitch"),
                (float) config.getDouble("maps." + mapId + ".defenderCoords.yaw")
        );
        return loc;
    }

    public ArrayList<NexusLocation> getNexusBlockLocations(String mapId) {
        ArrayList<NexusLocation> locations = new ArrayList<>();
        for (String str : config.getConfigurationSection("maps." + mapId + ".nexusLocations").getKeys(false)) {
            locations.add(new NexusLocation(str, new Location(
                    getWorld(mapId),
                    config.getInt("maps." + mapId + ".nexusLocations." + str + ".x"),
                    config.getInt("maps." + mapId + ".nexusLocations." + str + ".y"),
                    config.getInt("maps." + mapId + ".nexusLocations." + str + ".z")
            ),
                    config.getInt("maps." + mapId + ".nexusLocations." + str + ".health")
            ));
        }
        return locations;
    }

    public GameMap getMap(String mapId) {
        Location spawnLocation = getSpawnLocation(mapId);
        Location defenderSpawnLocation = getDefenderSpawnLocation(mapId);
        Location attackerSpawnLocation = getAttackerSpawnLocation(mapId);
        ArrayList<NexusLocation> nexusLocations = getNexusBlockLocations(mapId);

        GameMap map = new GameMap(_plugin, mapId, spawnLocation, defenderSpawnLocation, attackerSpawnLocation, nexusLocations);
        return map;
    }

    public World getWorld(String mapId) {
        World world = _plugin.getServer().getWorld(getWorldName(mapId));
        return world;
    }

    public void setEnabled(boolean value) {
        config.set("enabled", value);
    }

    public void save() {
        try {
            config.save(_plugin.getDataFolder() + "./config.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GameMap> getMaps() {
        List<GameMap> maps = new ArrayList<>();
        for (String str : config.getConfigurationSection("maps").getKeys(false)) {
            maps.add(getMap(str));
            System.out.println("MAP REGISTERED: " + str);
        }
        return maps;
    }
}
