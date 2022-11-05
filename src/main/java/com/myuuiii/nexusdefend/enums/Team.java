package com.myuuiii.nexusdefend.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Team {
    ATTACKERS(ChatColor.AQUA + "Attackers", Material.EMERALD_BLOCK),
    DEFENDERS(ChatColor.GOLD + "Defenders", Material.GOLD_BLOCK);

    private String display;
    private Material guiItem;

    Team(String display, Material guiItem) {
        this.display = display;
        this.guiItem = guiItem;
    }

    public String getDisplay() {
        return this.display;
    }

    public Material getMaterial() {
        return this.guiItem;
    }
}
