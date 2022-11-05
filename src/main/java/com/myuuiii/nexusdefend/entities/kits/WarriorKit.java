package com.myuuiii.nexusdefend.entities.kits;

import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.enums.KitType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.UUID;

public class WarriorKit extends Kit {
    public WarriorKit(NexusDefend plugin, UUID uuid) {
        super(plugin, KitType.WARRIOR, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().clear();

        // Weapons
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        player.getInventory().setItem(0, sword);

        ItemStack food = new ItemStack(Material.COOKED_BEEF, 16);
        player.getInventory().setItem(8, food);

        // Armor
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        helmet.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setHelmet(helmet);

        ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        chest.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setChestplate(chest);

        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        boots.addEnchantment(Enchantment.DURABILITY, 3);
        player.getInventory().setBoots(boots);
    }
}
