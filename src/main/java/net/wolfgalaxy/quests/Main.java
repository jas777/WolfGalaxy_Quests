package net.wolfgalaxy.quests;

import org.apache.logging.log4j.core.util.SystemClock;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public void onEnable(){
        instance = this;
        registerCommands();
        registerEvents();

        System.out.println("W[]olfGalaxy_Quests Enabled!");
    }

    public void onDisable(){



    }

    public void registerCommands(){

    }

    public void registerEvents(){



    }

    public static Main getInstance() {
        return instance;
    }//Okey hold on.

}
