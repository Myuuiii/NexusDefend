package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {
    private final NexusDefend _plugin;
    private final GameMap _map;
    private int gameTime;

    public GameTimer(NexusDefend plugin, GameMap map) {
        this._plugin = plugin;
        this._map = map;
        this.gameTime = ConfigManager.getGameTime(map.getId()) * 60;
    }

    @Override
    public void run() {
        if (gameTime == 0) {
            cancel();
            _map.endGame(false, true);
            return;
        }

        if (gameTime < 60) {
            _map.sendMessage("Game ending in " + gameTime);
        }

        gameTime--;
    }

    public void start() {
        runTaskTimer(_plugin, 0, 20);
    }
}
