package com.myuuiii.nexusdefend.commands;

import com.myuuiii.nexusdefend.GameMapManager;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.GameMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class NdCommand implements CommandExecutor {

    private NexusDefend _plugin;
    private GameMapManager _mapManager;
    public NdCommand(NexusDefend plugin) {
        this._plugin = plugin;
        this._mapManager = plugin.getMapManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "join":
                String mapId = args[1];
                System.out.println("MAP ID: " + mapId);
                GameMap joiningMap = _mapManager.getMap(mapId);
                boolean playerAdded = joiningMap.addPlayer(Objects.requireNonNull(sender.getServer().getPlayer(sender.getName())));
                if (!playerAdded) {
                    sender.sendMessage("You are already in this game");
                    return false;
                }
                sender.sendMessage("You have joined the game");
                sender.getServer().getPlayer(sender.getName()).teleport(joiningMap.LobbySpawn);
                break;
            case "leave":
                GameMap leavingMap = _mapManager.getMap(sender.getServer().getPlayer(sender.getName()));
                if (leavingMap == null) {
                    sender.sendMessage("You are currently not in a game");
                    return false;
                }
                assert leavingMap != null;
                leavingMap.removePlayer(Objects.requireNonNull(sender.getServer().getPlayer(sender.getName())));
                sender.sendMessage("You have left the game");
                break;
        }
        return false;
    }
}
