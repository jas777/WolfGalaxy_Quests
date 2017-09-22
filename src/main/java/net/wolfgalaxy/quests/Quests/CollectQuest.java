package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.Main;
import net.wolfgalaxy.quests.PlayerStatsManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CollectQuest extends Quest{

    private List<ItemStack> collectables;


    public CollectQuest() {
        super("CollectQuest", QuestType.Collect);
        collectables = new ArrayList<ItemStack>();
        final List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Material.GLASS, 16));
        items.add(new ItemStack(Material.GOLD_INGOT, 2));

        new BukkitRunnable() {

            public void run() {
                setCollectables(items);
            }

        }.runTaskLater(Main.getInstance(), 10l);

    }

    public void setCollectables(List<ItemStack> items) {
        collectables = items;
        File dataFile = getDataFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        List<String> collectablesStrings = new ArrayList<String>();
        for (ItemStack item : items ) {
            String itemString = item.getType().toString()+":"+item.getAmount();
            collectablesStrings.add(itemString);
        }
        config.set("Collectables",collectablesStrings);

        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void interact(Player player) {
        if (PlayerStatsManager.getMananager().hasCompletedQuest(player, this)) {
            ChatUtil.sendMessage(player, "You already have completed this quest!");
            return;
        }
        if (!PlayerStatsManager.getMananager().isStartedQuest(player,this)) {
            if (this.getRequirements().hasRequirements(player)) {
                this.getRequirements().takeRequirements(player);
                PlayerStatsManager.getMananager().startQuest(player, this);
                ChatUtil.sendMessage(player, "Quest: " + getQuestName() + " has started!");
                PrintQuestMessage(player);
                ChatUtil.sendMessage(player, "Can you bring me:");
                Inventory playerInv = player.getInventory();
                for (ItemStack collectable : collectables) {
                    if (playerInv.contains(collectable.getType(), collectable.getAmount())) {
                        ChatUtil.sendMessage(player, "&a+&7" + collectable.getAmount() + "x " + collectable.getType().toString());
                    } else {
                        ChatUtil.sendMessage(player, "&4-&7" + collectable.getAmount() + "x " + collectable.getType().toString());
                    }
                }
            }else {
                this.getRequirements().printRequirements(player);
            }
        }else {
            if (hasAllCollectables(player)) {
                for (ItemStack collectable : collectables) {
                    int amount = collectable.getAmount();
                    Inventory inv = player.getInventory();
                    Iterator<ItemStack> itemIt = Arrays.asList(inv.getContents()).iterator();
                    while (itemIt.hasNext()) {
                        ItemStack item = itemIt.next();
                        if (item == null || item.getType() == Material.AIR) continue;
                        if (item.getType() == collectable.getType() && item.getAmount() >= collectable.getAmount()) {
                            int rest = item.getAmount()-collectable.getAmount();
                            item.setAmount(rest);
                            break;
                        }
                    }
                }
                PlayerStatsManager.getMananager().compeleteQuest(player,this);
                giveReward(player);
                ChatUtil.sendMessage(player, "&6You have completed this quest!");
            }else {
                ChatUtil.sendMessage(player, "&cYou don't have all the supplies!");
                Inventory playerInv = player.getInventory();
                for (ItemStack collectable : collectables ) {
                    if (playerInv.contains(collectable.getType(), collectable.getAmount())) {
                        ChatUtil.sendMessage(player, "&a+&7"+collectable.getAmount()+"x "+collectable.getType().toString());
                    }else {
                        ChatUtil.sendMessage(player, "&4-&7"+collectable.getAmount()+"x "+collectable.getType().toString());
                    }
                }
            }
        }
    }

    private boolean hasAllCollectables(Player player) {
        ItemStack[] items = player.getInventory().getContents();
        int rightCounter = 0;
        for (ItemStack collectable : collectables) {
            for (ItemStack item : items ) {
                if (item == null || item.getType() == Material.AIR)continue;
                if (item.getType() == collectable.getType() && item.getAmount() == collectable.getAmount()) {
                    rightCounter++;
                }
            }
        }
        if (rightCounter == collectables.size()) {
            return true;
        }else {
            return false;
        }
    }
}
