package com.myuuiii.nexusdefend.gui;

import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamUI {
    public TeamUI(GameMap map, Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Team Selection");

        for (Team team : Team.values()) {
            ItemStack is = new ItemStack(team.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(team.getDisplay() + ChatColor.RESET + " (" + map.getTeamCount(team) + " players)");
            isMeta.setLocalizedName(team.name());
            is.setItemMeta(isMeta);
            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
