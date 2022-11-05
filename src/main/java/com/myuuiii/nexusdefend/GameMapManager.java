package com.myuuiii.nexusdefend;

import com.myuuiii.nexusdefend.entities.GameMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameMapManager {

    private List<GameMap> maps = new ArrayList<>();

    public GameMapManager(NexusDefend plugin) {
        ConfigManager config = plugin.getData();
        maps = config.getMaps();
    }

    public List<GameMap> getMaps() {
        return maps;
    }

    public GameMap getMap(String id) {
        for (GameMap map : maps) {
            if (Objects.equals(map.getId(), id)) return map;
            System.out.println("NOT MAP WITH ID " + map.getId());
        }
        return null;
    }

    public GameMap getMap(Player player) {
        for (GameMap map : maps) {
            if (map.Players.contains(player.getUniqueId())) {
                return map;
            }
        }
        return null;
    }
}
