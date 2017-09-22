package net.wolfgalaxy.quests;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {

    private static String prefix = "&8[&6WolfGalaxy &lQuests&r&8] &7";

    public static void sendMessage(ConsoleCommandSender sender, String message) {
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);
        sender.sendMessage(formattedMessage);
    }

    public static void sendMessage(Player sender, String message) {
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);
        sender.sendMessage(formattedMessage);
    }
}
