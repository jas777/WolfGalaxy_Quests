package net.wolfgalaxy.quests;

import net.wolfgalaxy.quests.Quests.QuestManager;
import net.wolfgalaxy.quests.Quests.QuestType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDExecutor implements CommandExecutor {

    private String label;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))return false;
        Player player = (Player) sender;
        this.label = label;
        if (args.length < 1){
            printHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("new")) {
            if (args.length < 2){
                printHelp(player);
                return true;
            }
            String questName = args[1];
            QuestManager.getManager().registerNewQuest(QuestType.Collect, questName, player);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2){
                printHelp(player);
                return true;
            }
            String questName = args[1];
            QuestManager.getManager().unregisterQuest(questName);
            return true;
        }

        return false;
    }

    private void printHelp(Player player) {
        String[] messages = new String[] {
                "&7=================&8[&6Quests Help&8]&7=================",
                "&7/&6"+label+" &8| &7to get the help message!",
                "&7/&6"+label+" &7new <QuestName> &8| &7to get the help message!",
                "&7/&6"+label+" &7remove <QuestName> &8| &7to get the help message!",
                "&7======================================================="
        };
        for (String line : messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',line));
        }
    }
}
