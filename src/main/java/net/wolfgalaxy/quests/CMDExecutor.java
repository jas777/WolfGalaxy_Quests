package net.wolfgalaxy.quests;

import net.wolfgalaxy.quests.Quests.*;
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

        if (label.equalsIgnoreCase("quest")) {
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
                QuestManager.getManager().unregisterQuest(questName, player);
                return true;
            }

            if (args[0].equalsIgnoreCase("assign")) {
                if (args.length < 3) {
                    printHelp(player);
                    return true;
                }
                String questerName = args[2];
                String questName = args[1];
                Quest quest = QuestManager.getManager().findQuestByName(questName);
                if (quest == null) {
                    ChatUtil.sendMessage(player, "&cQuest by name: " + questName + " could not be found!");
                    return true;
                }
                Quester quester = QuesterManager.getManager().findQuesterByName(questerName);
                if (quest == null) {
                    ChatUtil.sendMessage(player, "&cQuester by name: " + questerName + " could not be found!");
                    return true;
                }
                QuestManager.getManager().assignQuest(quest, quester);
                return true;
            }

            if (args[0].equalsIgnoreCase("unsign")) {
                if (args.length < 3) {
                    printHelp(player);
                    return true;
                }
                String questerName = args[2];
                String questName = args[1];
                Quest quest = QuestManager.getManager().findQuestByName(questName);
                if (quest == null) {
                    ChatUtil.sendMessage(player, "&cQuest by name: " + questName + " could not be found!");
                    return true;
                }
                Quester quester = QuesterManager.getManager().findQuesterByName(questerName);
                if (quest == null) {
                    ChatUtil.sendMessage(player, "&cQuester by name: " + questerName + " could not be found!");
                    return true;
                }
                QuestManager.getManager().unsignQuest(quest, quester);
                return true;
            }

            if(args[0].equalsIgnoreCase("list")) {
                QuestManager.getManager().sendQuestsList(player);
            }
        }

        if (label.equalsIgnoreCase("quester")) {
            if (args[0].equalsIgnoreCase("new")) {
                if (args.length < 2) {
                    printHelp(player);
                    return true;
                }
                String questerName = args[1];
                QuesterManager.getManager().registerNewQuester(questerName, player);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    printHelp(player);
                    return true;
                }
                String questerName = args[1];
                QuesterManager.getManager().unregisterQuest(questerName, player);
                return true;
            }

            if(args[0].equalsIgnoreCase("list")) {
                QuesterManager.getManager().sendQuestersList(player);
            }
        }

        return false;
    }

    private void printHelp(Player player) {
        String[] messages = new String[] {
                "&7=====================&8[&6Quests Help&8]&7=====================",
                "&7/&6"+label+" &8| &7to get the help message!",
                "&7/&6Quest &7new <QuestName> &8| &7to get the help message!",
                "&7/&6Quest &7remove <QuestName> &8| &7to get the help message!",
                "&7/&6Quest &7assign <QuestName> <QuesterName> &8| &7assign a quest to a quester!",
                "&7/&6Quest &7unsign <QuestName> <QuesterName> &8| &7unsign a quest from a quester!",
                "&7/&6Quest &7list &8| &7get a list of all quests!",
                "&7/&6Quester &7new <QuesterName> &8| &7to get the help message!",
                "&7/&6Quester &7remove <QuesterName> &8| &7to get the help message!",
                "&7/&6Quester &7list &8| &7get a list of all questers!",
                "&7====================================================="
        };
        for (String line : messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',line));
        }
    }
}
