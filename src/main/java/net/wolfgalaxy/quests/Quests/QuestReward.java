package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.PlayerStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuestReward {

    public static QuestReward minimum = new QuestReward();
    private List<ItemStack> items;
    private int experience;
    private List<String> permissions;

    public QuestReward() {
        this.items = new ArrayList<ItemStack>();
        this.experience = 0;
        this.permissions = new ArrayList<String>();

    }

    public void addItem(ItemStack item) {
        this.items.add(item);
    }

    public void setExperience(int amount) {
        this.experience = amount;
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
        this.experience = 0;
    }

    public void clearAll() {
        this.clearItems();
        this.clearExperience();
        this.clearPermissions();
    }

    public void giveReward(Player player) {
        Inventory inv = player.getInventory();

        for (ItemStack item : this.items ) {
            inv.addItem(item);
        }

        player.giveExp(this.experience);

        for (String permission : this.permissions ) {
            PlayerStatsManager.getMananager().addPermission( player, permission);
        }
    }
}
