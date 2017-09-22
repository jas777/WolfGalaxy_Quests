package net.wolfgalaxy.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreBoardManager implements Listener {

    private static ScoreBoardManager sm;
    private HashMap<UUID, Scoreboard> gameBoards;

    public ScoreBoardManager() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());

        createScoreboards();

        for (Player p : Main.getInstance().getServer().getOnlinePlayers()) {
            createGameBoard(p);
        }

        new BukkitRunnable() {

            public void run() {
                update();
            }

        }.runTaskTimer(Main.getInstance(), 10, 1);
    }

    private void createScoreboards() {
        gameBoards = new HashMap<UUID, Scoreboard>();
    }

    public void createGameBoard(Player p) {
        gameBoards.remove(p.getUniqueId());
        Scoreboard gameBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective quests = gameBoard.registerNewObjective("Quests", "dummy");
        quests.setDisplaySlot(DisplaySlot.SIDEBAR);
        quests.setDisplayName(ChatColor.GOLD+"Quest Board");

        //YourTeam
        quests.getScore(ChatColor.translateAlternateColorCodes('&', "&1")).setScore(4); //empty space;

        Team completedQuests = gameBoard.registerNewTeam("completedQuests");
        String entry0 = ChatColor.translateAlternateColorCodes('&', "&1&2&2");
        completedQuests.addEntry(entry0);

        completedQuests.setPrefix(ChatColor.translateAlternateColorCodes('&', "&Completed: "+ PlayerStatsManager.getMananager().getCompletedQuests(p).size()));

        quests.getScore(entry0).setScore(4);

        Team openQuests = gameBoard.registerNewTeam("openQuests");
        String entry1 = ChatColor.translateAlternateColorCodes('&', "&1&2&3");
        openQuests.addEntry(entry1);

        openQuests.setPrefix(ChatColor.translateAlternateColorCodes('&', "&Open: "+ PlayerStatsManager.getMananager().getOpenQuests(p).size()));

        quests.getScore(entry1).setScore(3);

        List<String> openQuestsList = PlayerStatsManager.getMananager().getOpenQuests(p);

        //

        Team Quest1 = gameBoard.registerNewTeam("Quest1");
        String questentry1 = ChatColor.translateAlternateColorCodes('&', "&2&3&4");
        Quest1.addEntry(questentry1);

        String Quest1Info = " ";
        if (openQuestsList.size() >= 1) {Quest1Info = openQuestsList.get(0);}
        Quest1.setPrefix(ChatColor.translateAlternateColorCodes('&', "&8- &7"+ Quest1Info));

        quests.getScore(questentry1).setScore(2);

        //

        Team Quest2 = gameBoard.registerNewTeam("Quest2");
        String questentry2 = ChatColor.translateAlternateColorCodes('&', "&2&3&5");
        Quest2.addEntry(questentry2);

        String Quest2Info = " ";
        if (openQuestsList.size() >= 2) {Quest2Info = openQuestsList.get(1);}
        Quest2.setPrefix(ChatColor.translateAlternateColorCodes('&', "&8- &7"+ Quest2Info));

        quests.getScore(questentry2).setScore(1);

        //

        Team Quest3 = gameBoard.registerNewTeam("Quest3");
        String questentry3 = ChatColor.translateAlternateColorCodes('&', "&2&3&6");
        Quest3.addEntry(questentry3);

        String Quest3Info = " ";
        if (openQuestsList.size() >= 3) {Quest3Info = openQuestsList.get(2);}
        Quest3.setPrefix(ChatColor.translateAlternateColorCodes('&', "&8- &7"+ Quest3Info));
        quests.getScore(questentry3).setScore(0);

        //



        gameBoards.put(p.getUniqueId(), gameBoard);

        p.setScoreboard(gameBoards.get(p.getUniqueId()));
    }

    protected void update() {
        for (Entry<UUID, Scoreboard> entry : gameBoards.entrySet()) {
            UUID playerUUID = entry.getKey();
            Player player = Bukkit.getPlayer(playerUUID);
            Scoreboard gameBoard = entry.getValue();

            gameBoard.getTeam("completedQuests").setPrefix(ChatColor.translateAlternateColorCodes('&', "&8Completed: &6"+ PlayerStatsManager.getMananager().getCompletedQuests(player).size()));

            gameBoard.getTeam("openQuests").setPrefix(ChatColor.translateAlternateColorCodes('&', "&8Open: &6"+ PlayerStatsManager.getMananager().getOpenQuests(player).size()));

            List<String> openQuestsList = PlayerStatsManager.getMananager().getOpenQuests(player);

            String Quest1Info = " ";
            if (openQuestsList.size() >= 1) {Quest1Info = openQuestsList.get(0);}
            gameBoard.getTeam("Quest1").setPrefix(ChatColor.translateAlternateColorCodes('&', "&8- &7"+ Quest1Info));

            String Quest2Info = " ";
            if (openQuestsList.size() >= 2) {Quest2Info = openQuestsList.get(1);}
            gameBoard.getTeam("Quest2").setPrefix(ChatColor.translateAlternateColorCodes('&', "&8- &7"+ Quest2Info));

            String Quest3Info = " ";
            if (openQuestsList.size() >= 3) {Quest3Info = openQuestsList.get(2);}
            gameBoard.getTeam("Quest3").setPrefix(ChatColor.translateAlternateColorCodes('&', "&8- &7"+ Quest3Info));
        }

    }

    public static ScoreBoardManager getManager() {
        if (sm == null)sm = new ScoreBoardManager();
        return sm;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        createGameBoard(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        gameBoards.remove(e.getPlayer().getUniqueId());
    }
}
