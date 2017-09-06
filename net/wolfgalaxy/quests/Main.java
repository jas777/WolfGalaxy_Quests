package net.wolfgalaxy.quests;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.Quests.Quest;
import net.wolfgalaxy.quests.Quests.QuestManager;
import net.wolfgalaxy.quests.Quests.QuesterManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
