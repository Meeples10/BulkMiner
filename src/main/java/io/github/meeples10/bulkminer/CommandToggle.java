package io.github.meeples10.bulkminer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandToggle implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("bulkminer.use")) {
            if(sender instanceof Player) {
                Bukkit.getPluginManager().getPlugin(Main.NAME).getLogger().info("Toggle: " + sender.getName());
                boolean value = Main.PREFERENCES.get(((Player) sender).getUniqueId());
                Main.PREFERENCES.put(((Player) sender).getUniqueId(), !value);
                sender.sendMessage(value ? Main.messageDisabled : Main.messageEnabled);
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
        } else {
            sender.sendMessage(Main.messageDenied);
        }
        return true;
    }
}
