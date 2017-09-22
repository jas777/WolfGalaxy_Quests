package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class QuestManager {

    private static QuestManager qm;
    private File _questFolder;
    private List<Quest> quests;

    public QuestManager() {
        quests = new ArrayList<Quest>();
        createQuestsFolder();
        loadAll();
    }

    public void saveAll() {
        Quest[] qs = (Quest[]) quests.toArray();
        for (Quest quest : qs) {
            quest.saveToDataFile();
        }
    }

    private void loadAll() {
        List<File> files = Arrays.asList(this._questFolder.listFiles());
        if (files == null || files.size() < 1 || files.isEmpty())return;
        Iterator<File> it = files.iterator();
        while (it.hasNext()) {
            File file = it.next();
            if (file.isDirectory() || !file.isFile())continue;
            loadQuestFile(file);
        }
    }

    private void loadQuestFile(File file) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (configuration == null) return;

        QuestType type = QuestType.valueOf(configuration.getString("Settings.QuestType"));
        String questName = configuration.getString("Settings.QuestName");
        Quest quest = type.createNew(questName);
        if (quest == null) {
            return;
        }

        quest.setDataFile(file);
        quests.add(quest);

        int id = configuration.getInt("Settings.NPC_ID");
        if (id == -1)return;
        Quester quester = QuesterManager.getManager().findQuesterById(id);
        if (quester == null) return;
        quest.setQuester(quester);
        quester.addQuest(quest);

        if (type == QuestType.Collect) {
            List<ItemStack> items = new ArrayList<ItemStack>();
            List<String> collectableStrings = configuration.getStringList("Collectables");
            for (String collableString : collectableStrings ) {
                String[] args = collableString.split(":");
                ItemStack item = new ItemStack(Material.valueOf(args[0]), Integer.parseInt(args[1]));
                items.add(item);
            }
            ((CollectQuest)quest).setCollectables(items);
        }
    }

    private void createQuestsFolder() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdirs();
        }

        this._questFolder = new File(Main.getInstance().getDataFolder(), "QuestsData");
        if (!this._questFolder.exists()) {
            this._questFolder.mkdir();
        }
    }

    public static QuestManager getManager() {
        if (qm == null)qm = new QuestManager();
        return qm;
    }

    public void registerNewQuest(QuestType questType, String questName, Player player) {
        Quest quest = questType.createNew(questName);
        if (quest == null) {
            ChatUtil.sendMessage(player, "&cQuest creation failed.");
            return;
        }

        File newquestData = new File(this._questFolder, questName+".yml");
        if (!newquestData.exists()) {
            try {
                newquestData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (newquestData == null) {
            ChatUtil.sendMessage(player, "&cQuest creation failed.");
            return;
        }
        quest.setDataFile(newquestData);
        quest.saveToDataFile();
        quests.add(quest);
        ChatUtil.sendMessage(player, "&aQuest created succesfully!");
    }

    public void registerNewQuest(QuestType questType, String questName) {
        Quest quest = questType.createNew(questName);
        if (quest == null) {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&cQuest creation failed.");
            return;
        }

        File newquestData = new File(this._questFolder, questName+".yml");
        if (!newquestData.exists()) {
            try {
                newquestData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (newquestData == null) {
            ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&cQuest creation failed.");
            return;
        }
        quest.setDataFile(newquestData);
        quest.saveToDataFile();
        quests.add(quest);
        ChatUtil.sendMessage(Bukkit.getConsoleSender(), "&aQuest created succesfully!");
    }

    protected void unregisterQuest(String questName) {
        Quest quest = null;
        for (Quest q : quests) {
            if (q.getQuestName().equalsIgnoreCase(questName)) {
                quest = q;
                break;
            }
        }
        if (quest == null) return;
        quests.remove(quest);
    }

    public void unregisterQuest(String questName, Player player) {
        Quest quest = null;
        for (Quest q : quests) {
            if (q.getQuestName().equalsIgnoreCase(questName)) {
                quest = q;
                break;
            }
        }
        if (quest == null) {
            ChatUtil.sendMessage(player, "&cQuest removing failed.");
            return;
        }
        quests.remove(quest);
        ChatUtil.sendMessage(player, "&aQuest removed succesfully!");
    }

    public Quest findQuestByName(String questname) {
        Quest quest = null;

        Iterator<Quest> questsIterator = quests.iterator();
        while (questsIterator.hasNext()) {
            Quest q = questsIterator.next();
            if (q.getQuestName().equalsIgnoreCase(questname)) {
                quest = q;
                break;
            }
        }
        if (quest == null)return null;

        return quest;
    }

    public void assignQuest(Quest quest, Quester quester) {
        if (quest == null || quester == null) return;
        quest.setQuester(quester);
        quester.addQuest(quest);
    }

    public void unsignQuest(Quest quest, Quester quester) {
        if (quest == null || quester == null) return;
        quest.setQuester(null);
        quester.removeQuest(quest);
    }

    public void sendQuestsList(Player player) {
        player.sendMessage("");
        ChatUtil.sendMessage(player, "List of all quests:");
        for (Quest q : quests) {
            ChatUtil.sendMessage(player, "- &6"+q.getQuestName());
        }
    }
}
