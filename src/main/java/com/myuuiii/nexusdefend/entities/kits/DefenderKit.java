package com.myuuiii.nexusdefend.entities.kits;

import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.enums.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class DefenderKit extends Kit{
    public DefenderKit(NexusDefend plugin, UUID uuid) {
        super(plugin, KitType.ARCHER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().clear();

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta itemMeta = sword.getItemMeta();
        itemMeta.setUnbreakable(true);
        sword.setItemMeta(itemMeta);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setItem(0, sword);

        ItemStack empireWand = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta wandMeta = empireWand.getItemMeta();
        wandMeta.setDisplayName(ChatColor.GOLD + "Empire wand");
        empireWand.setItemMeta(wandMeta);
        player.getInventory().setItem(1, empireWand);

        // Armor
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setHelmet(helmet);

        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setChestplate(chest);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setBoots(boots);

        // Healing Items
        ItemStack Gapples = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 16);
        player.getInventory().setItem(7, Gapples);

        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        player.getInventory().setItem(8, food);
    }
}
