package com.myuuiii.nexusdefend.entities.kits;

import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.enums.KitType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.UUID;

public class ArcherKit extends Kit {

    public ArcherKit(NexusDefend plugin, UUID uuid) {
        super(plugin, KitType.ARCHER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().clear();

        // Weapons
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1);

        ItemMeta itemMeta = bow.getItemMeta();
        itemMeta.setUnbreakable(true);
        bow.setItemMeta(itemMeta);

        player.getInventory().setItem(0, bow);

        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        player.getInventory().setItem(10, arrow);

        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.setItemMeta(itemMeta);
        player.getInventory().setItem(1, sword);

        ItemStack food = new ItemStack(Material.COOKED_BEEF, 16);
        player.getInventory().setItem(8, food);

        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        player.getInventory().setItem(2, pickaxe);

        // Armor
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta coloredItemMeta = (LeatherArmorMeta) helmet.getItemMeta();
        coloredItemMeta.setColor(Color.fromRGB(10, 100, 0));
        helmet.setItemMeta(coloredItemMeta);
        player.getInventory().setHelmet(helmet);

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        chest.setItemMeta(coloredItemMeta);
        player.getInventory().setChestplate(chest);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        boots.setItemMeta(coloredItemMeta);
        player.getInventory().setBoots(boots);
    }
}
