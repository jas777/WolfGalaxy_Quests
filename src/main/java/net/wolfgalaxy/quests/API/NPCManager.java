package net.wolfgalaxy.quests.API;

import net.wolfgalaxy.quests.Main;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import java.util.UUID;

public class NPCManager implements Listener {
	
	private static NPCManager nm;
	private NPCRegistry registry;

	public NPCManager() {
		registry = CitizensAPI.getNPCRegistry();
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}//Done

	public static NPCManager getManager() {
		if (nm == null)nm = new NPCManager();
		return nm;
	}

	/**
	 * This method is used to create a new NPC.
	 * @param displayName the name to display
	 * @param loc the location were the npc needs to be
	 * @param entityType the type of entity it needs to be
	 * @return the entity that is created.
	 */
	public NPC createNPC(String displayName, Location loc, EntityType entityType) {
		NPC npc = registry.createNPC(entityType,displayName);
		npc.spawn(loc);
		return npc;
	}

	public NPC findNPCByID(int ID) {
		return registry.getById(ID);
	}

}
