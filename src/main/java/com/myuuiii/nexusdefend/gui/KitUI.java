package com.myuuiii.nexusdefend.gui;

import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.enums.KitType;
import com.myuuiii.nexusdefend.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class KitUI {
    public KitUI(GameMap map, Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Kit Selection");

        for (KitType kit : KitType.values()) {
            if (kit == KitType.DEFENDER) continue;
            ItemStack is = new ItemStack(kit.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(kit.getDisplay() + ChatColor.RESET + " (" + map.getKitCount(kit) + " players)");
            isMeta.setLore(Arrays.asList(kit.getDescription()));
            isMeta.setLocalizedName(kit.name());
            is.setItemMeta(isMeta);
            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
