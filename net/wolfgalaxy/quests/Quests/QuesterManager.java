package net.wolfgalaxy.quests.Quests;

import net.citizensnpcs.api.npc.NPC;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.ChatUtil;
import net.wolfgalaxy.quests.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class QuesterManager {

    private static QuesterManager qsm;
    private List<Quester> questers;
    private File _questerFolder;

    public QuesterManager() {
        questers = new ArrayList<Quester>();
        createQuestersFolder();
        loadAll();
    }

    public void saveAll() {
        Quester[] qs = (Quester[]) questers.toArray();
        for (Quester quester : qs) {
            quester.saveToDataFile();
        }
    }

    private void loadAll() {
        List<File> files = Arrays.asList(this._questerFolder.listFiles());
        if (files == null || files.size() < 1 || files.isEmpty())return;
        Iterator<File> it = files.iterator();
        while (it.hasNext()) {
            File file = it.next();
            if (file.isDirectory() || !file.isFile())continue;
            loadQuesterFile(file);
        }
    }

    private void loadQuesterFile(File file) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (configuration == null) return;

        String questerName = configuration.getString("Settings.QuesterName");

        int id = configuration.getInt("Settings.NPC_ID");
        if (id == (-1) )return;
        NPC npc = NPCManager.getManager().findNPCByID(id);
        Quester quester = new Quester(npc);
        if (quester == null) return;
        quester.setDataFile(file);
        questers.add(quester);
        System.out.println(questerName + "Created!");
    }

    public void registerNewQuester(String questerName, Player player) {
        NPC npc = NPCManager.getManager().createNPC(questerName, player.getLocation(), EntityType.PLAYER);
        Quester quester = new Quester(npc);
        if (quester == null) {
            ChatUtil.sendMessage(player, "&cQuester creation failed.");
            return;
        }

        File newquesterData = new File(this._questerFolder, questerName+".yml");
        if (!newquesterData.exists()) {
            try {
                newquesterData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            quester.setDataFile(newquesterData);
        }
        if (newquesterData == null) {
            ChatUtil.sendMessage(player, "&cQuester creation failed.");
            return;
        }
        quester.setDataFile(newquesterData);
        quester.saveToDataFile();
        questers.add(quester);
        ChatUtil.sendMessage(player, "&aQuester created succesfully!");
    }

    public void registerNewQuester(String questerName, NPC npc) {
        Quester quester = new Quester(npc);
        if (quester == null) {
            return;
        }

        File newquesterData = new File(this._questerFolder, questerName+".yml");
        if (!newquesterData.exists()) {
            try {
                newquesterData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (newquesterData == null) {
            return;
        }
        quester.setDataFile(newquesterData);
        quester.saveToDataFile();
        questers.add(quester);
    }

    protected void unregisterQuest(String questerName) {
        Quester quester = null;
        for (Quester q : questers) {
            if (q.getQuesterName().equalsIgnoreCase(questerName)) {
                quester = q;
                break;
            }
        }
        if (quester == null) return;
        questers.remove(quester);
    }

    public void unregisterQuest(String questerName, Player player) {
        Quester quester = null;
        for (Quester q : questers) {
            if (q.getQuesterName().equalsIgnoreCase(questerName)) {
                quester = q;
                break;
            }
        }
        if (quester == null) {
            ChatUtil.sendMessage(player, "&cQuester removing failed.");
            return;
        }
        questers.remove(quester);
        ChatUtil.sendMessage(player, "&aQuester removed succesfully!");
    }

    private void createQuestersFolder() {
        if (!Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdirs();
        }

        this._questerFolder = new File(Main.getInstance().getDataFolder(), "QuestersData");
        if (!this._questerFolder.exists()) {
            this._questerFolder.mkdir();
        }
    }

    public static QuesterManager getManager() {
        if (qsm == null) {
            qsm = new QuesterManager();
        }
        return qsm;
    }

    public Quester findQuesterById(int id) {
        Quester quester = null;
        if (questers == null || questers.isEmpty())return null;
        for (Quester q : questers) {
            if (q.getId() == id) {
                quester = q;
            }
        }

        return quester;
    }


    public Quester findQuesterByName(String questerName) {
        Quester quester = null;
        if (questers == null || questers.isEmpty())return null;
        for (Quester q : questers) {
            if (q.getQuesterName() == questerName) {
                quester = q;
            }
        }

        return quester;
    }

    public void sendQuestersList(Player player) {
        player.sendMessage("");
        ChatUtil.sendMessage(player, "List of all questers:");
        for (Quester q : questers) {
            ChatUtil.sendMessage(player, "- &6"+q.getQuesterName());
        }
    }
}
