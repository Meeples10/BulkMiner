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

import com.google.common.collect.ImmutableMap;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
    public static final String NAME = "BulkMiner";
    public static final Map<Material, Material> ORE_VARIANTS = ImmutableMap.copyOf(new HashMap<Material, Material>() {
        private static final long serialVersionUID = -5043644644304100583L;
        {
            put(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE);
            put(Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE);
            put(Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE);
            put(Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
            put(Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE);
            put(Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE);
            put(Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE);
            put(Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE);
            put(Material.DEEPSLATE_COAL_ORE, Material.COAL_ORE);
            put(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_ORE);
            put(Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND_ORE);
            put(Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD_ORE);
            put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_ORE);
            put(Material.DEEPSLATE_IRON_ORE, Material.IRON_ORE);
            put(Material.DEEPSLATE_LAPIS_ORE, Material.LAPIS_ORE);
            put(Material.DEEPSLATE_REDSTONE_ORE, Material.REDSTONE_ORE);
        }
    });
    public static final Map<UUID, Boolean> PREFERENCES = new HashMap<>();
    public static final List<Material> ENABLED_BLOCKS = new ArrayList<>();
    public static final List<Material> ENABLED_TOOLS = new ArrayList<>();

    public static boolean enabledByDefault;
    public static boolean survivalOnly;
    public static boolean dropXp;
    public static int maxDistance;
    public static String messageEnabled;
    public static String messageDisabled;
    public static String messageDenied;
    private static File df, cfg, prefs;
    public static boolean variantsAreSame;

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
            enabledByDefault = c.getBoolean("enabled-by-default", true);
            survivalOnly = c.getBoolean("survival-only", true);
            dropXp = c.getBoolean("drop-xp", true);
            maxDistance = c.getInt("max-distance", 5);
            messageEnabled = ChatColor.translateAlternateColorCodes('&', c.getString("message-enabled"));
            messageDisabled = ChatColor.translateAlternateColorCodes('&', c.getString("message-disabled"));
            messageDenied = ChatColor.translateAlternateColorCodes('&', c.getString("message-denied"));
            for(String s : c.getStringList("enabled-blocks")) {
                ENABLED_BLOCKS.add(Material.getMaterial(s));
            }
            for(String s : c.getStringList("enabled-tools")) {
                ENABLED_TOOLS.add(Material.getMaterial(s));
            }
            variantsAreSame = c.getBoolean("variants-are-same", true);
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
