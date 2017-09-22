package net.wolfgalaxy.quests;

import net.wolfgalaxy.quests.Quests.Quest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerStatsManager {
    private static PlayerStatsManager pm;
    private File playerStatsFolder;

    public PlayerStatsManager() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdirs();
        }
        playerStatsFolder = new File(Main.getInstance().getDataFolder(), "PlayerStats");
        if (!playerStatsFolder.exists()) {
            playerStatsFolder.mkdirs();
        }
    }

    private File getPlayerFile(Player player) {
        UUID uuid = player.getUniqueId();
        File playerFile = new File(playerStatsFolder, uuid + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeDefaults(playerFile, player);
        }
        return playerFile;
    }

    private void writeDefaults(File file, Player player) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("PlayerDetails.Name", player.getName());
        config.set("PlayerDetails.UniqueId", player.getUniqueId().toString());
        config.set("UnlockedPermissions", new ArrayList<String>());
        config.set("CompletedQuests", new ArrayList<String>());
        config.set("StartedQuests", new ArrayList<String>());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerStatsManager getMananager() {
        if (pm == null) pm = new PlayerStatsManager();
        return pm;
    }

    public boolean hasPermission(Player player, String permission) {
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(getPlayerFile(player));
        List<String> permissions = playerFile.getStringList("UnlockedPermissions");
        Iterator<String> it = permissions.iterator();
        while (it.hasNext()) {
            String perm = it.next();
            if (permission.equalsIgnoreCase(perm)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCompletedQuest(Player player, Quest quest) {
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(getPlayerFile(player));
        List<String> completedQuests = playerFile.getStringList("CompletedQuests");
        Iterator<String> it = completedQuests.iterator();
        String questName = quest.getQuestName();
        while (it.hasNext()) {
            String q = it.next();
            if (questName.equalsIgnoreCase(q)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStartedQuest(Player player, Quest quest) {
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(getPlayerFile(player));
        List<String> startedQuests = playerFile.getStringList("StartedQuests");
        Iterator<String> it = startedQuests.iterator();
        String questName = quest.getQuestName();
        while (it.hasNext()) {
            String q = it.next();
            if (questName.equalsIgnoreCase(q)) {
                return true;
            }
        }
        return false;
    }

    public void addPermission(Player player, String permission) {
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(getPlayerFile(player));
        List<String> permissions = playerFile.getStringList("UnlockedPermissions");
        Iterator<String> it = permissions.iterator();
        while (it.hasNext()) {
            String perm = it.next();
            if (permission.equalsIgnoreCase(perm)) {
                it.remove();
            }
        }
        permissions.add(permission);
        playerFile.set("UnlockedPermissions",permissions);

        try {
            playerFile.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePermission(Player player, String permission) {
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(getPlayerFile(player));
        List<String> permissions = playerFile.getStringList("UnlockedPermissions");
        Iterator<String> it = permissions.iterator();
        while (it.hasNext()) {
            String perm = it.next();
            if (permission.equalsIgnoreCase(perm)) {
                it.remove();
            }
        }
        playerFile.set("UnlockedPermissions",permissions);

        try {
            playerFile.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startQuest(Player player, Quest quest) {
        File pf = getPlayerFile(player);
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(pf);
        String questName = quest.getQuestName();

        List<String> startedQuests = playerFile.getStringList("StartedQuests");
        Iterator<String> startedIt = startedQuests.iterator();
        while (startedIt.hasNext()) {
            String q = startedIt.next();
            if (questName.equalsIgnoreCase(q)) {
                startedIt.remove();
            }
        }
        startedQuests.add(questName);
        playerFile.set("StartedQuests", startedQuests);

        try {
            playerFile.save(pf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compeleteQuest(Player player, Quest quest) {
        File pf = getPlayerFile(player);
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(pf);
        List<String> startedQuests = playerFile.getStringList("StartedQuests");
        Iterator<String> startedIt = startedQuests.iterator();
        String questName = quest.getQuestName();
        while (startedIt.hasNext()) {
            String q = startedIt.next();
            if (questName.equalsIgnoreCase(q)) {
                startedIt.remove();
            }
        }
        playerFile.set("StartedQuests", startedQuests);

        List<String> completedQuests = playerFile.getStringList("CompletedQuests");
        Iterator<String> completedIt = completedQuests.iterator();
        while (completedIt.hasNext()) {
            String q = completedIt.next();
            if (questName.equalsIgnoreCase(q)) {
                completedIt.remove();
            }
        }
        completedQuests.add(questName);
        playerFile.set("CompletedQuests", completedQuests);

        try {
            playerFile.save(pf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getOpenQuests(Player player) {
        File pf = getPlayerFile(player);
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(pf);
        return playerFile.getStringList("StartedQuests");
    }

    public List<String> getCompletedQuests(Player player) {
        File pf = getPlayerFile(player);
        FileConfiguration playerFile = YamlConfiguration.loadConfiguration(pf);
        return playerFile.getStringList("CompletedQuests");
    }
}
