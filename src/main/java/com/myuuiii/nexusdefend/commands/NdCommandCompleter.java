package com.myuuiii.nexusdefend.commands;

import com.myuuiii.nexusdefend.NexusDefend;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NdCommandCompleter implements TabCompleter {
    public NdCommandCompleter(NexusDefend plugin) {
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0], Arrays.asList("join", "leave", "team", "kit"), new ArrayList<>());
        }
        return null;
    }
}
