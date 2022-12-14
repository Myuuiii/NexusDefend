package com.myuuiii.nexusdefend.listeners;

import com.myuuiii.nexusdefend.NexusDefend;
import com.myuuiii.nexusdefend.entities.GameMap;
import com.myuuiii.nexusdefend.entities.NexusLocation;
import com.myuuiii.nexusdefend.enums.GameState;
import com.myuuiii.nexusdefend.enums.KitType;
import com.myuuiii.nexusdefend.enums.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GameListener implements Listener {
    private final NexusDefend plugin;

    public GameListener(NexusDefend nexusDefend) {
        this.plugin = nexusDefend;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // TEAM SELECTION
        if (event.getInventory() != null && event.getCurrentItem() != null && event.getView().getTitle().contains("Team Selection")) {
            Team team = Team.valueOf(event.getCurrentItem().getItemMeta().getLocalizedName());

            GameMap map = plugin.getMapManager().getMap(player);
            if (map != null) {
                if (map.getTeam(player) == team) {
                    player.sendMessage("You are already on this team");
                } else {
                    map.setTeam(player, team);
                }
            }
            player.closeInventory();
            event.setCancelled(true);
        }

        // KIT SELECTION
        if (event.getInventory() != null && event.getCurrentItem() != null && event.getView().getTitle().contains("Kit Selection")) {
            KitType kit = KitType.valueOf(event.getCurrentItem().getItemMeta().getLocalizedName());

            GameMap map = plugin.getMapManager().getMap(player);
            if (map != null) {
                if (map.getKitType(player) == kit) {
                    player.sendMessage("You are already have this kit selected");
                } else {
                    map.setKit(player.getUniqueId(), kit);
                }
            }
            player.closeInventory();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        GameMap map = plugin.getMapManager().getMap(event.getPlayer());
        if (map != null) {
            event.setCancelled(true);
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
                                event.setCancelled(true);
                                return;
                            }
                            map.getGameInfo().damageNexus(event.getPlayer(), nexusId);

                            // End the game if all the nexuses have been destroyed
                            map.getGameInfo().checkNexusStatesAndEndGameIfAllDestroyed();
                        }
                    }
                }
            }
        }
    }
}
