package com.myuuiii.nexusdefend.commands;

import com.myuuiii.nexusdefend.GameMapManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.enums.GameState;
import com.myuuiii.nexusdefend.gui.TeamUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class NdCommand implements CommandExecutor {

    private final NexusDefend _plugin;
    private final GameMapManager _mapManager;

    public NdCommand(NexusDefend plugin) {
        this._plugin = plugin;
        this._mapManager = plugin.getMapManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "join":
                String mapId = args[1];
                System.out.println("MAP ID: " + mapId);
                GameMap joiningMap = _mapManager.getMap(mapId);
                boolean playerAdded = joiningMap.addPlayer(player);
                if (!playerAdded) {
                    sender.sendMessage("You are already in this game");
                    return false;
                }
                sender.sendMessage("You have joined the game");
                player.teleport(joiningMap.LobbySpawn);
                break;
            case "leave":
                GameMap leavingMap = _mapManager.getMap(player);
                if (leavingMap == null) {
                    sender.sendMessage("You are currently not in a game");
                    return false;
                }
                leavingMap.removePlayer(Objects.requireNonNull(player));
                sender.sendMessage("You have left the game");
                break;
            case "team":
                if (!player.hasPermission("nd.team_select")) {
                    player.sendMessage("You are not allowed to select your team");
                    return false;
                }
                GameMap teamSelectMap = _mapManager.getMap(player);
                if (teamSelectMap == null) {
                    sender.sendMessage("You are currently not in a game");
                    return false;
                }
                if (teamSelectMap.getState() != GameState.Live) {
                    new TeamUI(teamSelectMap, player);
                } else {
                    sender.sendMessage("You can not use this right now");
                }
                break;
        }
        return false;
    }
}
