package net.wolfgalaxy.quests.Quests;

import net.citizensnpcs.api.npc.NPC;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.Quests.CollectQuest;
import net.wolfgalaxy.quests.Quests.Quest;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public enum QuestType {

    Collect(CollectQuest.class);

    private Class<Quest> _questType;

    QuestType(Class<?> c) {
        _questType = (Class<Quest>) c;
    }

    public Quest createNew(String questName) {
        Quest quest = null;
        try {
            quest = _questType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (quest == null)return null;

        quest.setQuestName(questName);

        return quest;
    }
}
