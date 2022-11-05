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

public class DestroyerKit extends Kit {

    public DestroyerKit(NexusDefend plugin, UUID uuid) {
        super(plugin, KitType.ARCHER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().clear();

        // Weapons
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().setItem(1, sword);

        ItemStack food = new ItemStack(Material.COOKED_BEEF, 16);
        player.getInventory().setItem(8, food);

        ItemStack pickaxe = new ItemStack(Material.GOLDEN_PICKAXE, 1);
        player.getInventory().setItem(0, pickaxe);

        // Armor
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta coloredItemMeta = (LeatherArmorMeta)chest.getItemMeta();
        coloredItemMeta.setColor(Color.fromRGB(100, 100, 0));
        chest.setItemMeta(coloredItemMeta);
        player.getInventory().setChestplate(chest);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        boots.setItemMeta(coloredItemMeta);
        player.getInventory().setBoots(boots);
    }
}
