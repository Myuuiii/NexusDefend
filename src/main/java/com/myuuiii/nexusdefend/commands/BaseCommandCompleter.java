package com.myuuiii.nexusdefend.commands;

import com.myuuiii.nexusdefend.ConfigManager;
import com.myuuiii.nexusdefend.NexusDefend;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseCommandCompleter implements TabCompleter {
    NexusDefend _plugin;
    ConfigManager _data;
    public BaseCommandCompleter(NexusDefend plugin) {
        this._plugin = plugin;
        this._data = _plugin.getData();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0], Arrays.asList("enable", "disable", "showMaps", "forceEnd"), new ArrayList<>());
        }
        return null;
    }
}
