package net.wolfgalaxy.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

    private static String prefix = "&8[&6WolfGalaxy &lQuests&r&8] &7";

    public static void sendMessage(Player player, String message) {
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', prefix + message);
        player.sendMessage(formattedMessage);
    }
}
