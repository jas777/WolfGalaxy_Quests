package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.PlayerStatsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class QuestRequirement {

    public static QuestRequirement minimum = new QuestRequirement();
    private List<ItemStack> items;
    private int experienceLevel;
    private List<String> permissions;

    public QuestRequirement() {
        this.items = new ArrayList<ItemStack>();
        this.permissions = new ArrayList<String>();
        this.experienceLevel = 0;
    }

    public void addItem(ItemStack item) {
        this.items.add(item);
    }

    public void setExperienceLevel(int newLevel) {
        this.experienceLevel = newLevel;
    }

    public void addPermission(String permission) {
        this.permissions.remove(permission);
        this.permissions.add(permission);
    }

    public void removePermission(String permission) {
        this.permissions.remove(permission);
    }

    public void clearPermissions() {
        this.permissions = new ArrayList<String>();
    }

    public void clearItems() {
        this.items = new ArrayList<ItemStack>();
    }

    public void clearExperience() {
        this.experienceLevel = 0;
    }

    public void clearAll() {
        this.clearItems();
        this.clearExperience();
        this.clearPermissions();
    }

    public void takeRequirements(Player player) {
        Inventory inv = player.getInventory();

        for (ItemStack item : this.items ) {
            inv.remove(item);
        }

        int currentLevel = player.getLevel();
        player.setLevel(currentLevel-this.experienceLevel);
    }

    public boolean hasRequirements(Player player) {
        Inventory inv = player.getInventory();

        for (ItemStack item : this.items ) {
            if (!inv.contains(item))return false;
        }

        if (player.getLevel() < this.experienceLevel)return false;

        for (String permission : this.permissions ) {
            if (!PlayerStatsManager.getMananager().hasPermission( player, permission)) return false;
        }
        return true;
    }

    public void printRequirements(Player player) {
        ChatUtil.sendMessage(player, "&cYou don't have all the requirements!");

        if (player.getLevel() >= experienceLevel) {
            ChatUtil.sendMessage(player, "&a+&7EXPLevel: "+experienceLevel);
        }else {
            ChatUtil.sendMessage(player, "&4-&7EXPLevel: "+experienceLevel);
        }

        Inventory playerInv = player.getInventory();
        for (ItemStack item : items ) {
            if (playerInv.contains(item.getType(), item.getAmount())) {
                ChatUtil.sendMessage(player, "&a+&7"+item.getAmount()+"x "+item.getType().toString());
            }else {
                ChatUtil.sendMessage(player, "&4-&7"+item.getAmount()+"x "+item.getType().toString());
            }
        }

        for (String permission : permissions ) {
            if (PlayerStatsManager.getMananager().hasPermission(player, permission)) {
                ChatUtil.sendMessage(player, "&a+&7Permission: "+permission);
            }else {
                ChatUtil.sendMessage(player, "&4-&7Permission: "+permission);
            }
        }
    }
}
