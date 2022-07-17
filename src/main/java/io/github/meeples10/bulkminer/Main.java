package io.github.meeples10.bulkminer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
    public static final String NAME = "BulkMiner";
    public static final Map<UUID, Boolean> PREFERENCES = new HashMap<>();
    public static final List<Material> ENABLED_BLOCKS = new ArrayList<>();
    public static final List<Material> ENABLED_TOOLS = new ArrayList<>();

    public static boolean enabledByDefault = true;
    public static boolean survivalOnly = true;
    public static boolean dropXp = true;
    public static int maxDistance = 5;
    public static String messageEnabled;
    public static String messageDisabled;
    public static String messageDenied;
    private static File df, cfg, prefs;

    @Override
    public void onEnable() {
        df = Bukkit.getServer().getPluginManager().getPlugin(NAME).getDataFolder();
        cfg = new File(df, "config.yml");
        prefs = new File(df, "prefs.yml");
        this.getCommand("bulkminer").setExecutor(new CommandToggle());
        Bukkit.getPluginManager().registerEvents(new BulkMinerListener(), Bukkit.getPluginManager().getPlugin(NAME));
        loadConfig();
    }

    @Override
    public void onDisable() {
        FileConfiguration c = YamlConfiguration.loadConfiguration(prefs);
        for(UUID uuid : PREFERENCES.keySet()) {
            c.set(uuid.toString(), PREFERENCES.get(uuid));
        }
        try {
            c.save(prefs);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean loadConfig() {
        try {
            if(!df.exists()) {
                df.mkdirs();
            }
            if(!cfg.exists()) {
                Bukkit.getPluginManager().getPlugin(NAME).saveDefaultConfig();
            }
            FileConfiguration c = YamlConfiguration.loadConfiguration(cfg);
            enabledByDefault = c.getBoolean("enabled-by-default");
            survivalOnly = c.getBoolean("survival-only");
            dropXp = c.getBoolean("drop-xp");
            maxDistance = c.getInt("max-distance");
            messageEnabled = ChatColor.translateAlternateColorCodes('&', c.getString("message-enabled"));
            messageDisabled = ChatColor.translateAlternateColorCodes('&', c.getString("message-disabled"));
            messageDenied = ChatColor.translateAlternateColorCodes('&', c.getString("message-denied"));
            for(String s : c.getStringList("enabled-blocks")) {
                ENABLED_BLOCKS.add(Material.getMaterial(s));
            }
            for(String s : c.getStringList("enabled-tools")) {
                ENABLED_TOOLS.add(Material.getMaterial(s));
            }
            FileConfiguration p = YamlConfiguration.loadConfiguration(prefs);
            for(String key : p.getKeys(false)) {
                PREFERENCES.put(UUID.fromString(key), p.getBoolean(key));
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
