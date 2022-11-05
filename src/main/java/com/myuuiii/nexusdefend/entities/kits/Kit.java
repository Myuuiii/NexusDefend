package com.myuuiii.nexusdefend.entities.kits;

import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.enums.KitType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class Kit implements Listener {
    private KitType type;
    private UUID uuid;

    public Kit(NexusDefend plugin, KitType type, UUID uuid) {
        this.uuid = uuid;
        this.type = type;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public KitType getType() {
        return type;
    }

    public abstract void onStart(Player player);

    public void remove() {
        HandlerList.unregisterAll(this);
    }
}
