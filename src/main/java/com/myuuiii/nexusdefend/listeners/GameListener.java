package com.myuuiii.nexusdefend.listeners;

import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.entities.NexusLocation;
import com.myuuiii.nexusdefend.enums.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GameListener implements Listener {
    private NexusDefend plugin;

    public GameListener(NexusDefend nexusDefend) {
        this.plugin = nexusDefend;
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        GameMap map = plugin.getMapManager().getMap(event.getPlayer());
        if (map != null) {
            if (map.getState().equals(GameState.Live)) {
                if (event.getBlock().getType() == Material.COAL_BLOCK) {
                    for (NexusLocation loc : map.NexusLocations) {
                        if (
                                loc.getLocation().getX() == event.getBlock().getLocation().getX() &&
                                        loc.getLocation().getY() == event.getBlock().getLocation().getY() &&
                                        loc.getLocation().getZ() == event.getBlock().getLocation().getZ()) {
                            String nexusId = loc.getNexusId();
                            if (map.getGameInfo().getNexusHealth(nexusId) <= 0) {
                                event.getPlayer().sendMessage("This nexus has already been destroyed");
                                return;
                            }
                            map.getGameInfo().damageNexus(event.getPlayer(), nexusId);
                            event.setCancelled(true);
                        }
                    }

                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
