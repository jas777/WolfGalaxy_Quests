package net.wolfgalaxy.quests.Quests;

import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.wolfgalaxy.quests.API.NPCManager;
import net.wolfgalaxy.quests.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Quest implements Listener {

    private String _questName;
    private NPC npc;

    public Quest(String questName) {
        this._questName = questName;

        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void initialize(String questName, Player player) {
        this._questName = questName;
        this.npc = NPCManager.getManager().createNPC(_questName,player.getLocation(), EntityType.PLAYER);
    }

    public String getQuestName() {
        return this._questName;
    }

    public void despawn() {
        if (npc == null)return;
        npc.despawn(DespawnReason.REMOVAL);
    }
}
