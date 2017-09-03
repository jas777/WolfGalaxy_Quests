package net.wolfgalaxy.quests.Quests;

import net.citizensnpcs.api.npc.NPC;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        for (Quest quest : quests) {
            quest.saveToDataFile();
        }
    }

    private void loadAll() {
        File[] files = this._questFolder.listFiles();
        if (files == null || files.length < 1)return;
        for (File file : files) {
            loadQuestFile(file);
        }
    }

    private void loadQuestFile(File file) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (configuration == null) return;

        QuestType type = QuestType.valueOf(configuration.getString("Settings.QuestType"));
        String questName = configuration.getString("Settings.QuestName");
        int id = configuration.getInt("Settings.NPC_ID");
        NPC npc = NPCManager.getManager().findNPCByID(id);
        if (npc == null) {
            System.out.println("NPC by id "+id+" could not be found!");
            return;
        }
        Quest quest = type.createNew(questName,npc);

        if (quest == null) {
            return;
        }

        quest.setDataFile(file);
        quests.add(quest);
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
        Quest quest = questType.createNew(questName,player.getLocation());
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

    public void unregisterQuest(String questName) {
        Quest quest = null;
        for (Quest q : quests) {
            if (q.getQuestName().equalsIgnoreCase(questName)){
                quest = q;
                break;
            }
        }
        if (quest == null)return;
        quest.despawn();
        quests.remove(quest);
    }

    /**
     * Get the Quest instance by the given NPC;
     * @param npc the NPC to find the quest from
     * @return the allocated quest, null if not found.
     */
    public Quest findQuestByNPC(NPC npc) {
        Quest quest = null;
        Entity entity = npc.getEntity();

        for (Quest q : quests) {
            if (q.getEntity().equals(entity)) {
                quest = q;
            }
        }
        return quest;
    }
}
