package com.myuuiii.nexusdefend.enums;

import org.bukkit.Material;

public enum KitType {
    WARRIOR("Warrior", Material.DIAMOND_SWORD, "For close range combat"),
    ARCHER("Archer", Material.BOW, "For ranged combat"),
    HEALER("Healer", Material.POTION, "Help your allies by healing them"),
    DESTROYER("Destroyer", Material.GOLDEN_PICKAXE, "Quickly damage a nexus, limited durability"),
    DEFENDER("Defender", Material.NETHER_STAR, "Defend the nexuses");

    private final String display;
    private final Material guiItem;
    private final String description;

    KitType(String display, Material guiItem, String description) {
        this.display = display;
        this.guiItem = guiItem;
        this.description = description;
    }

    public String getDisplay() {
        return this.display;
    }

    public Material getMaterial() {
        return guiItem;
    }

    public String getDescription() {
        return description;
    }
}
