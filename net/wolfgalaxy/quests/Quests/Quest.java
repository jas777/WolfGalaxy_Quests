package net.wolfgalaxy.quests.Quests;

import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.Main;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Quest implements Listener {

    private String _questName;
    private QuestType _questType;


    private File _dataFile;
    private QuestRequirement requirements;

    private Quester quester;

    public Quest(String questName, QuestType type) {
        this._questName = questName;
        this._questType = type;
        this.requirements = QuestRequirement.minimum;

        requirements = new QuestRequirement();

        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }



    protected void setQuestName(String questName) {
        this._questName = questName;
    }

    protected void setQuester(Quester quester) {
        this.quester = quester;
        saveToDataFile();
    }

    public String getQuestName() {
        return this._questName;
    }

    protected void setDataFile(File dataFile) {
        this._dataFile = dataFile;
    }

    protected void saveToDataFile() {
        if (this._dataFile == null)return;

        FileConfiguration dataConfiguration = YamlConfiguration.loadConfiguration(this._dataFile);

        dataConfiguration.set("Settings.QuestName",this._questName);
        dataConfiguration.set("Settings.NPC_ID",(this.quester == null)? -1 : this.quester.getId() );
        dataConfiguration.set("Settings.QuestType",this._questType.toString());

        try {
            dataConfiguration.save(this._dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setRequirements(QuestRequirement requirements) {
        this.requirements = requirements;
    }

    public abstract void interact(Player player);
}
