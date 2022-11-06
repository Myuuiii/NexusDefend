package com.myuuiii.nexusdefend.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Team {
    ATTACKERS(ChatColor.AQUA + "Attackers", Material.EMERALD_BLOCK),
    DEFENDERS(ChatColor.GOLD + "Defenders", Material.GOLD_BLOCK);

    private final String display;
    private final Material guiItem;

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
