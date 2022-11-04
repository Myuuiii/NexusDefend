package com.myuuiii.nexusdefend;

import java.util.ArrayList;
import java.util.List;

public class GameMapManager {

    private List<GameMap> maps = new ArrayList<>();

    public GameMapManager(NexusDefend plugin) {
        ConfigManager config = plugin.getData();
    }
}
