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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class HealerKit extends Kit {
    public HealerKit(NexusDefend plugin, UUID uuid) {
        super(plugin, KitType.HEALER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().clear();

        // Weapons
        ItemStack regenerationPotions = new ItemStack(Material.SPLASH_POTION, 16);
        PotionMeta regenerationPotionMeta = (PotionMeta) regenerationPotions.getItemMeta();
        regenerationPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 2, false), true);
        regenerationPotions.setItemMeta(regenerationPotionMeta);
        player.getInventory().setItem(0, regenerationPotions);

        ItemStack absorptionPotions = new ItemStack(Material.SPLASH_POTION, 8);
        PotionMeta absorptionPotionMeta = (PotionMeta) absorptionPotions.getItemMeta();
        absorptionPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 15, 10, false), true);
        absorptionPotions.setItemMeta(absorptionPotionMeta);
        player.getInventory().setItem(1, absorptionPotions);

        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta itemMeta = sword.getItemMeta();
        itemMeta.setUnbreakable(true);
        sword.setItemMeta(itemMeta);
        sword.addEnchantment(Enchantment.KNOCKBACK, 2);
        player.getInventory().setItem(3, sword);

        ItemStack food = new ItemStack(Material.COOKED_BEEF, 16);
        player.getInventory().setItem(8, food);

        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.setItemMeta(itemMeta);
        player.getInventory().setItem(4, pickaxe);

        // Armor
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta coloredItemMeta = (LeatherArmorMeta) helmet.getItemMeta();
        coloredItemMeta.setColor(Color.fromRGB(100, 10, 0));
        helmet.setItemMeta(coloredItemMeta);
        player.getInventory().setHelmet(helmet);
    }
}
