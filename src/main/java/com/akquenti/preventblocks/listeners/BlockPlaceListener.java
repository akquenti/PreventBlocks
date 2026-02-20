package com.akquenti.preventblocks.listeners;

import com.akquenti.preventblocks.PreventBlocks;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        // Check if player has bypass permission
        if (player.hasPermission("preventblocks.bypass")) {
            return;
        }
        
        // Check if player is in a blocked world
        if (!isInBlockedWorld(player)) {
            return;
        }
        
        // Check if this block is blocked
        if (isBlockBlocked(blockType)) {
            event.setCancelled(true);
            
            // Send message to player
            if (PreventBlocks.getInstance().getConfig().getBoolean("notify-player", true)) {
                player.sendMessage(PreventBlocks.getInstance().getMessage("message"));
            }
            
            // Notify admins
            if (PreventBlocks.getInstance().getConfig().getBoolean("notify-admins", true)) {
                notifyAdmins(player, blockType);
            }
        }
    }
    
    private boolean isInBlockedWorld(Player player) {
        String worlds = PreventBlocks.getInstance().getConfig().getString("worlds", "world_the_end");
        String[] worldNames = worlds.split(",");
        
        // Check by world name
        for (String worldName : worldNames) {
            if (player.getWorld().getName().equalsIgnoreCase(worldName.trim())) {
                return true;
            }
        }
        
        // Check by world type (THE_END)
        return player.getWorld().getEnvironment() == World.Environment.THE_END;
    }
    
    private boolean isBlockBlocked(Material blockType) {
        List<String> blockedBlocks = PreventBlocks.getInstance().getConfig()
            .getStringList("blocked-blocks");
        String mode = PreventBlocks.getInstance().getConfig().getString("mode", "blacklist");
        
        // Convert string list to materials for comparison
        boolean isInList = false;
        for (String blockName : blockedBlocks) {
            try {
                Material material = Material.valueOf(blockName.toUpperCase());
                if (material == blockType) {
                    isInList = true;
                    break;
                }
            } catch (IllegalArgumentException e) {
                // If material not found, ignore and continue
                PreventBlocks.getInstance().getLogger().warning("Unknown material in config: " + blockName);
            }
        }
        
        // Return result based on mode
        if (mode.equalsIgnoreCase("whitelist")) {
            return !isInList; // In whitelist mode, only blocks from list are allowed
        } else {
            return isInList; // In blacklist mode, blocks from list are blocked
        }
    }
    
    private void notifyAdmins(Player player, Material blockType) {
        String message = String.format(
            "&e[PreventBlocks] &c%s &etried to place &c%s &ein world &c%s",
            player.getName(),
            blockType.name(),
            player.getWorld().getName()
        );
        
        // Notify all online admins
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("preventblocks.admin")) {
                onlinePlayer.sendMessage(PreventBlocks.getInstance().getMessage(message));
            }
        }
        
        // Also log to console
        PreventBlocks.getInstance().getLogger().info(
            player.getName() + " tried to place " + blockType.name() + 
            " in world " + player.getWorld().getName()
        );
    }
}