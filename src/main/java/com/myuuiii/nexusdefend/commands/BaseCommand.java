package com.myuuiii.nexusdefend.commands;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.GameMap;
import com.myuuiii.nexusdefend.NexusDefend;
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
            sender.sendMessage("SPAWN COORDS: " + "X:" + map.LobbySpawn.getX() + "Y:" + map.LobbySpawn.getY() + "Z:" + map.LobbySpawn.getZ());
            sender.sendMessage("ATTACKER SPAWN COORDS: " + "X:" + map.AttackerSpawn.getX() + "Y:" + map.AttackerSpawn.getY() + "Z:" + map.AttackerSpawn.getZ());
            sender.sendMessage("DEFENDER SPAWN COORDS: " + "X:" + map.DefenderSpawn.getX() + "Y:" + map.DefenderSpawn.getY() + "Z:" + map.DefenderSpawn.getZ());
//            sender.sendMessage("Nexus Count: " + map.NexusLocations.stream().count());
        }
    }

    private void togglePluginState(boolean value, CommandSender sender, String message) {
        _data.setEnabled(value);
        sender.sendMessage(message);
    }
}
