package com.myuuiii.nexusdefend.commands;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.GameMap;
import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.NexusLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class BaseCommand implements CommandExecutor {

    NexusDefend _plugin;
    ConfigManager _data;

    public BaseCommand(NexusDefend plugin) {
        this._plugin = plugin;
        this._data = _plugin.getData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "enable":
                togglePluginState(true, sender, "The Nexus Defend plugin has been enabled");
                break;
            case "disable":
                togglePluginState(false, sender, "The Nexus Defend plugin has been disabled");
                break;
            case "state":
                break;
            case "showMaps":
                showMaps(sender);
                break;
        }
        return false;
    }

    private void showMaps(CommandSender sender) {
        ArrayList<String> listOfMaps = _data.getWorldIds();
        for (String str : listOfMaps) {
            sender.sendMessage("Map ID: " + str);
            GameMap map = _data.getMap(str);
            sender.sendMessage("    SPAWN COORDS: " + "[ " + map.LobbySpawn.getX() + " " + map.LobbySpawn.getY() + " " + map.LobbySpawn.getZ() + " ]");
            sender.sendMessage("    ATTACKER SPAWN COORDS: " + "[ " + map.AttackerSpawn.getX() + " " + map.AttackerSpawn.getY() + " " + map.AttackerSpawn.getZ() + " ]");
            sender.sendMessage("    DEFENDER SPAWN COORDS: " + "[ " + map.DefenderSpawn.getX() + " " + map.DefenderSpawn.getY() + " " + map.DefenderSpawn.getZ() + " ]");
            sender.sendMessage("    Nexus Count: " + map.NexusLocations.stream().count());
            for (NexusLocation loc : map.NexusLocations) {
                sender.sendMessage("        NEXUS " + "[ " + loc.Location.getX() + " " + loc.Location.getY() + " " + loc.Location.getZ() + " ]");
            }
        }
    }

    private void togglePluginState(boolean value, CommandSender sender, String message) {
        _data.setEnabled(value);
        sender.sendMessage(message);
    }
}
