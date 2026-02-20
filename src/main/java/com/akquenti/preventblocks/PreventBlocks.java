package com.akquenti.preventblocks;

import com.akquenti.preventblocks.listeners.BlockPlaceListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PreventBlocks extends JavaPlugin {
    
    private static PreventBlocks instance;
    private FileConfiguration config;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        loadConfig();
        
        // Register listener
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        
        getLogger().info("PreventBlocks has been enabled!");
        getLogger().info("Blocking configured blocks in specified worlds");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("PreventBlocks has been disabled");
    }
    
    private void loadConfig() {
        reloadConfig();
        config = getConfig();
        
        // Add default settings
        config.addDefault("message", "&cYou cannot place this block here!");
        config.addDefault("worlds", "world_the_end");
        config.addDefault("mode", "blacklist");
        config.addDefault("notify-admins", true);
        config.addDefault("notify-player", true);
        
        // Add default blocked blocks list
        if (!config.contains("blocked-blocks")) {
            config.set("blocked-blocks", Arrays.asList(
                "END_PORTAL_FRAME",
                "END_PORTAL",
                "END_GATEWAY",
                "DRAGON_EGG",
                "RESPAWN_ANCHOR",
                "BEDROCK",
                "COMMAND_BLOCK",
                "CHAIN_COMMAND_BLOCK",
                "REPEATING_COMMAND_BLOCK",
                "STRUCTURE_BLOCK",
                "BARRIER"
            ));
        }
        
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("preventblocks")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("preventblocks.admin")) {
                        reloadConfig();
                        loadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");

                        int blockedBlocks = getConfig().getStringList("blocked-blocks").size();
                        sender.sendMessage(ChatColor.YELLOW + "Blocked blocks: " + blockedBlocks);
                        sender.sendMessage(ChatColor.YELLOW + "Mode: " + getConfig().getString("mode"));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission!");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (sender.hasPermission("preventblocks.admin")) {
                        sender.sendMessage(ChatColor.GOLD + "=== Blocked Blocks ===");
                        List<String> blocks = getConfig().getStringList("blocked-blocks");
                        for (String block : blocks) {
                            sender.sendMessage(ChatColor.YELLOW + "- " + block);
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (sender.hasPermission("preventblocks.admin")) {
                        sender.sendMessage(ChatColor.GOLD + "=== PreventBlocks Info ===");
                        sender.sendMessage(ChatColor.YELLOW + "Plugin folder: " + getDataFolder().getAbsolutePath());
                        sender.sendMessage(ChatColor.YELLOW + "Config exists: " + new File(getDataFolder(), "config.yml").exists());
                        sender.sendMessage(ChatColor.YELLOW + "Server folder: " + getServer().getWorldContainer().getAbsolutePath());
                        return true;
                    }
                }
            }

            // Command help
            sender.sendMessage(ChatColor.GOLD + "=== PreventBlocks ===");
            sender.sendMessage(ChatColor.YELLOW + "/pb reload " + ChatColor.WHITE + "- reload configuration");
            sender.sendMessage(ChatColor.YELLOW + "/pb list " + ChatColor.WHITE + "- show blocked blocks list");
            sender.sendMessage(ChatColor.YELLOW + "/pb info " + ChatColor.WHITE + "- show plugin info");
            return true;
        }
        return false;
    }
    
    public static PreventBlocks getInstance() {
        return instance;
    }
    
    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', 
            getConfig().getString(path, "&cAction blocked!"));
    }
}