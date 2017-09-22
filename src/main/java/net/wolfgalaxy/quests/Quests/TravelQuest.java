package net.wolfgalaxy.quests.Quests;

import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.PlayerStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;

public class TravelQuest extends Quest {

    private Location targetLoc;

    public TravelQuest() {
        super("TravelQuest", QuestType.Collect);//QuestType needs to be Travel but commented because of incomplete class TravelQuest.
        targetLoc = new Location(Bukkit.getWorlds().get(0), 0,0,0);
    }

    public void setTargetLoc(Location targetLoc) {
        this.targetLoc = targetLoc;
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
                ChatUtil.sendMessage(player, "Travel location:");
                ChatUtil.sendMessage(player, "   World: &6"+targetLoc.getWorld().getName());
                ChatUtil.sendMessage(player, "   Xpos: &6"+targetLoc.getBlockX());
                ChatUtil.sendMessage(player, "   Ypos: &6"+targetLoc.getBlockY());
                ChatUtil.sendMessage(player, "   Zpos: &6"+targetLoc.getBlockZ());
                startTravelFor(player);
            }else {
                this.getRequirements().printRequirements(player);
            }
        }else {
            ChatUtil.sendMessage(player, "&cYou've already started this quest!");
            ChatUtil.sendMessage(player, "Travel location:");
            ChatUtil.sendMessage(player, "   World: &6"+targetLoc.getWorld().getName());
            ChatUtil.sendMessage(player, "   Xpos: &6"+targetLoc.getBlockX());
            ChatUtil.sendMessage(player, "   Ypos: &6"+targetLoc.getBlockY());
            ChatUtil.sendMessage(player, "   Zpos: &6"+targetLoc.getBlockZ());
        }
    }

    private void startTravelFor(Player player) {
    }

    public boolean isPlayerAtTargetLocation(Location playerLocation) {
        if (playerLocation.getWorld().getName().equalsIgnoreCase(targetLoc.getWorld().getName())) {
            int x = playerLocation.getBlockX();
            int y = playerLocation.getBlockY();
            int z = playerLocation.getBlockZ();
            if (x == targetLoc.getBlockX() && y == targetLoc.getBlockY() && z == targetLoc.getBlockZ()) {
                return true;
            }
        }
        return false;
    }
}
