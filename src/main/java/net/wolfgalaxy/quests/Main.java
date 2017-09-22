package net.wolfgalaxy.quests;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.Quests.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sun.awt.windows.ThemeReader;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;

    public void onEnable() {
        instance = this;
        if (!getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            System.out.println("Requirements failure!");
            System.out.println("Missing Plugin: Citizens");
            getPluginLoader().disablePlugin(this);
        }
        System.out.println("Quests enabled.");

        initialize();
    }

    private void initialize() {
        new BukkitRunnable() {
            public void run() {
                registerManagers();

                registerCommands();

                registerEvents();
            }
        }.runTaskLaterAsynchronously(instance,1);
        ScoreBoardManager.getManager();
    }


    private void APIExample() {
        //This is a tutorial for using the QuestsAPI

        Location location = new Location(Bukkit.getWorld("world"), 193,75,237);
        QuesterManager.getManager().registerNewQuester("TestQuester", location);//Creates a new Quester at the given location.
        Quester quester = QuesterManager.getManager().findQuesterByName("TestQuester");//Gets the TestQuester from the data storage.

        QuestManager.getManager().registerNewQuest(QuestType.Collect, "TestQuest");//Creates a new quest called TestQuest.

        Quest quest = QuestManager.getManager().findQuestByName("TestQuest");//Gets the TestQuest from the data storage.

        // If the quest is of type Collect you can do this:
        List<ItemStack> collectables = new ArrayList<ItemStack>();
        collectables.add(new ItemStack(Material.COBBLESTONE, 5));//Adds 5 cobblestone to the collectables;
        ((CollectQuest)quest).setCollectables(collectables);

        //To set a reward you can do this:
        QuestReward reward = QuestReward.minimum; //you can do 'QuestReward.minimum' or 'new QuestReward()' its just what you like.
        reward.addItem(new ItemStack(Material.DIAMOND_BLOCK, 1));//Adds a diamondBlock to the rewards.
        reward.addPermission("TestPermission");//Adds the 'TestPermission' to the rewards.
        reward.setExperience(20);//Sets the experience reward.
        quest.setReward(reward);//Sets the rewards for the quest.

        // If you want to add requirements for starting a quest you can do this:
        QuestRequirement requirement = QuestRequirement.minimum; //you can do 'QuestRequirement.minimum' or 'new QuestRequirement()' its just what you like.
        requirement.addItem(new ItemStack(Material.DIAMOND, 1));// Adds a item as requirement.
        requirement.setExperienceLevel(5);// Adds a experience level as requirement.
        requirement.addPermission("Test");// Adds a permission as requirement.
        quest.setRequirements(requirement);//Sets the requirements for starting a quest.

        QuestManager.getManager().assignQuest(quest, quester);//Assigns the TestQuest to the TestQuester.

        //And you just created a new Quester with assigned quest with rewards!


    }

    private void registerManagers() {
        QuestManager.getManager();
        QuesterManager.getManager();
        NPCManager.getManager();
    }

    public void onDisable(){
        QuesterManager.getManager().saveAll();
        QuestManager.getManager().saveAll();
    }

    private void registerCommands(){

        CMDExecutor cmdExecutor = new CMDExecutor();
        getCommand("quest").setExecutor(cmdExecutor);
        getCommand("quester").setExecutor(cmdExecutor);

    }

    private void registerEvents(){

    }

    public static Main getInstance() {
        return instance;
    }

}
