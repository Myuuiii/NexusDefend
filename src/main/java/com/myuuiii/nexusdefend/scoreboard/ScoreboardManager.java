package com.myuuiii.nexusdefend.scoreboard;

import com.myuuiii.nexusdefend.entities.GameInfo;
import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.entities.NexusLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final GameInfo gameInfo;
    private final Player player;
    private Scoreboard scoreboard;
    private GameMap map;
    private Objective obj;

    public ScoreboardManager(GameMap map, Player player) {
        this.map = map;
        this.gameInfo = map.getGameInfo();
        this.player = player;
        scoreboard = player.getScoreboard();
        obj = scoreboard.registerNewObjective("NexusHealth", Criteria.DUMMY, "Nexus Health");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateScoreboard() {
        obj.setDisplayName(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + map.timer.getTimeRemaining() + ChatColor.GOLD + "]");
        obj.getScore(" ").setScore(69);
        for (NexusLocation nexus : map.NexusLocations) {
            Score nexusScore = obj.getScore("Nexus " + nexus.getNexusId());
            nexusScore.setScore(gameInfo.getNexusHealth(nexus.getNexusId()));
        }
        player.setScoreboard(scoreboard);
    }

    public void dispose() {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
