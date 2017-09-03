package net.wolfgalaxy.quests;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.Quests.Quest;
import net.wolfgalaxy.quests.Quests.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private static Main instance;

    public void onEnable(){
        instance = this;
        if (!getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            System.out.println("Requirements failure!");
            System.out.println("Missing Plugin: Citizens");
            getPluginLoader().disablePlugin(this);
        }
        System.out.println("Quests enabled.");

        initialize();

        //I have a NPC manager we can use. hold on
    }

    private void initialize() {
        new BukkitRunnable() {

            public void run() {
                registerManagers();
                registerCommands();
                registerEvents();
            }
        }.runTaskLaterAsynchronously(instance,5);
    }

    private void registerManagers() {
        QuestManager.getManager();
        NPCManager.getManager();
    }

    public void onDisable(){
        QuestManager.getManager().saveAll();
    }

    public void registerCommands(){

        getCommand("quest").setExecutor(new CMDExecutor());

    }

    public void registerEvents(){

    }

    public static Main getInstance() {
        return instance;
    }

}
