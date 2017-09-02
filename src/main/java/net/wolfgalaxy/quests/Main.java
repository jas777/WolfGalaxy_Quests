package net.wolfgalaxy.quests;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.wolfgalaxy.quests.Quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public void onEnable(){
        instance = this;
        System.out.println("Quests enabled.");

        registerCommands();
        registerEvents();

        //I have a NPC manager we can use. hold on
    }
    public void onDisable(){


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
