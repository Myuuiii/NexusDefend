package com.myuuiii.nexusdefend.entities;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.enums.GameState;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private final NexusDefend _plugin;
    private final GameMap _map;
    private int countdownTime;

    public Countdown(NexusDefend plugin, GameMap map) {
        this._plugin = plugin;
        this._map = map;
        this.countdownTime = ConfigManager.getCountdownTime();
    }

    @Override
    public void run() {
        if (countdownTime == 0) {
            cancel();
            _map.start();
            return;
        }

        if (countdownTime <= 10 || countdownTime % 15 == 0) {
            _map.sendMessage("Starting in " + ChatColor.GREEN + countdownTime + " second" + (countdownTime == 1 ? "" : "s"));
        }

        countdownTime--;
    }

    public void start() {
        _map.setState(GameState.Countdown);
        runTaskTimer(_plugin, 0, 20);
    }
}
