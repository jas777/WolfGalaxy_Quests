package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.ChatUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestManager {

    private static QuestManager qm;
    private List<Quest> quests;

    public QuestManager() {
        quests = new ArrayList<Quest>();
    }

    public static QuestManager getManager() {
        if (qm == null)qm = new QuestManager();
        return qm;
    }

    public void registerNewQuest(QuestType questType, String questName, Player player) {
        Quest quest = questType.createNew(questName,player);
        if (quest == null) {
            ChatUtil.sendMessage(player, "&cQuest creation failed.");
            return;
        }
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
}
