package net.wolfgalaxy.quests.Quests;

import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.wolfgalaxy.quests.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Quester implements Listener {

    private HashMap<String, ItemStack> ItemList;
    private String questerName;
    private NPC _npc;
    private File _dataFile;
    private List<Quest> quests;

    private Inventory _questGUI;

    public Quester(NPC npc) {
        this.questerName = npc.getName();
        this._npc = npc;
        quests = new ArrayList<Quest>();
        _questGUI = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',"&5Quests"));
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }



    private void openQuest(Player player) {
        createGUI(player);
        player.openInventory(_questGUI);
    }

    private int minIndex = 10;
    private int maxIndex = 44;

    private Inventory createGUI(Player player) {
        ItemList = new HashMap<String, ItemStack>();
        resetGUI();

        int counter = minIndex-1;
        Iterator<Quest> questsIterator = quests.iterator();
        while (questsIterator.hasNext()) {
            Quest q = questsIterator.next();
            counter++;
            if (counter > maxIndex) {
                System.out.println("Quests Limit reached for quester: "+questerName);
                break;
            }
            CreateSlotItem(_questGUI, Material.PAPER,1,new int[] {0},null,new String[] {},new HashMap<Enchantment, Integer>(),ChatColor.translateAlternateColorCodes('&',"&7Quest: &9"+q.getQuestName()),q.getQuestName(),new int[] {counter},false);

        }
        return _questGUI;
    }

    private void resetGUI() {
        _questGUI.clear();
        ItemList.clear();

    }

    /**
     * @param inventory = the inventory to put the item into.
     * @param material = the material for the item.
     * @param amount = amount of items of that type.
     * @param data = the data of that item.
     * @param color = the Color of that item.
     * @param lores = a list of lores to add to that item.
     * @param enchantments = the enchantments that need to be added to that item.
     * @param displayName = the display name of the item.
     * @param getterName = the name to retrieve the item from the hashmap.
     * @param SlotNumbers = the slotnumbers were we should put the items into.
     * @param Repeater = if you use multiple item slots.
     */
    private void CreateSlotItem(Inventory inventory, Material material, int amount, int[] data, Color color, String[] lores, HashMap<Enchantment,Integer> enchantments, String displayName, String getterName, int[] SlotNumbers, boolean Repeater) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                data[i] = 0;
            }
            if (data[i] > 127) {
                data[i] = 127;
            }
        }
        ItemStack item = new ItemStack(material, amount, (byte) data[0]);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lores.length > 0) {
            List<String> _lores = new ArrayList<String>();
            for (String line : lores) {
                _lores.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(_lores);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        if (item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_HELMET) {
            LeatherArmorMeta armourmeta = (LeatherArmorMeta) item.getItemMeta();
            armourmeta.setColor(color);
            item.setItemMeta(armourmeta);
        }
        if (enchantments != null) {
            for (Map.Entry<Enchantment, Integer> compiledmap : enchantments.entrySet()) {
                Enchantment ench = compiledmap.getKey();
                int level = compiledmap.getValue();
                item.addUnsafeEnchantment(ench, level);
            }
        }
        if (Repeater) {
            int Counter = 0;
            for (int i = 0; i < SlotNumbers.length; i++) {
                if (SlotNumbers[i] > 53) {
                    SlotNumbers[i] = 53;
                }
                item = new ItemStack(material, amount, (byte) data[Counter]);
                inventory.setItem(SlotNumbers[i], item);
                if (getterName != null) {
                    ItemList.put(getterName + i, item);
                }
                Counter++;
                if (Counter >= data.length) {
                    Counter = 0;
                }
            }
        }
        if (SlotNumbers[0] > 53) {
            SlotNumbers[0] = 53;
        }
        inventory.setItem(SlotNumbers[0], item);
        if (getterName != null) {
            ItemList.put(getterName, item);
        }
    }

    public void despawn() {
        if (_npc == null)return;
        _npc.despawn(DespawnReason.REMOVAL);
    }

    /**
     * Gets the entity the quest is allocated to.
     * @return the allocated entity.
     */
    public Entity getEntity() {
        return _npc.getEntity();
    }

    public String getQuesterName() {
        return this.questerName;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() == null || e.getRightClicked() instanceof NPC)return;
        Entity entity = e.getRightClicked();
        if (this.getEntity().equals(entity)) {
            this.openQuest(e.getPlayer());
        }
    }

    @EventHandler
    public void onGUIInteract(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(this._questGUI))return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName())return;
        e.setCancelled(true);
        if (e.getClick() != ClickType.LEFT || e.getClick() == ClickType.DOUBLE_CLICK)return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
        Iterator<Map.Entry<String, ItemStack>> entryIterator = ItemList.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, ItemStack> entry = entryIterator.next();
            if (itemName.equalsIgnoreCase(entry.getValue().getItemMeta().getDisplayName())) {
                String questName = entry.getKey();
                Quest quest = QuestManager.getManager().findQuestByName(questName);
                quest.interact(player);
            }
        }
    }

    public int getId() {
        return _npc.getId();
    }

    public void setDataFile(File dataFile) {
        this._dataFile = dataFile;
    }

    protected void saveToDataFile() {
        if (this._dataFile == null)return;

        FileConfiguration dataConfiguration = YamlConfiguration.loadConfiguration(this._dataFile);

        dataConfiguration.set("Settings.QuesterName",this.questerName);
        dataConfiguration.set("Settings.NPC_ID",(this._npc == null)? -1 : this._npc.getId() );

        try {
            dataConfiguration.save(this._dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addQuest(Quest quest) {
        if (quests.contains(quest))return;
        quests.add(quest);
    }

    public void removeQuest(Quest quest) {
        quests.remove(quest);
    }
}
