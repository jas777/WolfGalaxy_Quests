package net.wolfgalaxy.quests.Quests;

import org.bukkit.inventory.ItemStack;

public class QuestRequirement {

    public static QuestRequirement minimum = new QuestRequirement();
    private ItemStack[] items;
    private int experienceLevel;

    public QuestRequirement() {
        this.items = new ItemStack[0];
        this.experienceLevel = 0;
    }

    public void addItem(ItemStack item) {
        this.items[this.items.length-1] = item;
    }

    public void setExperienceLevel(int newLevel) {
        this.experienceLevel = newLevel;
    }

    public void clearItems() {
        this.items = new ItemStack[0];
    }

    public void clearExperience() {
        this.experienceLevel = 0;
    }

    public void clearAll() {
        this.clearItems();
        this.clearExperience();
    }
}
