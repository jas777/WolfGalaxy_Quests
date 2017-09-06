package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.Quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CollectQuest extends Quest{

    private ItemStack[] collectables;


    public CollectQuest() {
        super("CollectQuest", QuestType.Collect);
        collectables = new ItemStack[0];
    }

    public void setCollectables(ItemStack[] items) {
        collectables = items;
    }

    public void interact(Player player) {
        ChatUtil.sendMessage(player, "Quest: "+getQuestName()+" has started!");
    }
}
