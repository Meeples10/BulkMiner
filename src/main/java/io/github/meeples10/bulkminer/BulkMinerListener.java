package io.github.meeples10.bulkminer;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BulkMinerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!Main.PREFERENCES.containsKey(e.getPlayer().getUniqueId())) {
            Main.PREFERENCES.put(e.getPlayer().getUniqueId(), Main.enabledByDefault);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(!e.getPlayer().hasPermission("bulkminer.use")) return;
        if(Main.PREFERENCES.get(e.getPlayer().getUniqueId())) {
            if(Main.ENABLED_TOOLS.contains(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                if(Main.ENABLED_BLOCKS.contains(e.getBlock().getType())) {
                    if(e.getPlayer().getGameMode() == GameMode.SURVIVAL || !Main.survivalOnly) {
                        Block block = e.getBlock();
                        if(block.getDrops(e.getPlayer().getInventory().getItemInMainHand()).size() > 0) {
                            Miner.mine(e.getBlock(), e.getPlayer(), e.getExpToDrop());
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
