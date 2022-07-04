package net.notlord.magic;

import net.md_5.bungee.api.ChatColor;
import net.notlord.Main;
import net.notlord.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.formatter.qual.ReturnsFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrimoireHandler implements Listener {
	private static class SpellRecipe{
		private final List<Object> objects;
		public final String spellDisplayName;
		public final int tier;
		public final int slotCost;

		public SpellRecipe(List<Object> objects, String spellDisplayName, int tier, int slotCost) {
			this.objects = objects;
			this.spellDisplayName = spellDisplayName;
			this.tier = tier;
			this.slotCost = slotCost;
		}

		public List<Object> getObjects() {
			return new ArrayList<>(objects);
		}
	}

	private static final Map<String, SpellRecipe> recipes = new HashMap<>();
	private static final Map<String, Inventory> inventories = new HashMap<>();
	private final Main plugin;

	public GrimoireHandler(Main plugin) {
		this.plugin = plugin;
	}

	public static void addSpellRecipe(String spellId, List<Object> list, String spellDisplayName, int tier, int slots){
		recipes.put(spellId, new SpellRecipe(new ArrayList<>(list), spellDisplayName,tier,slots));
	}

	public static String tierToString(int tier){
		switch (tier){
			case 1 -> {
				return "basic";
			}
			case 2 -> {
				return "advanced";
			}
			case 3 -> {
				return "high";
			}
			case 4 -> {
				return "royal";
			}
			case 5 -> {
				return "forbidden";
			}
		}
		return "";
	}

	public static void openGrimoire(Player player, int max_spell_tier){
		if(!inventories.containsKey(player.getUniqueId().toString())){
			inventories.put(player.getUniqueId().toString(), Bukkit.createInventory(null,54, ChatColor.WHITE+"Grimoire"));
		}
		Inventory inventory = inventories.get(player.getUniqueId().toString());
		Item item = new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
		for(int i=0;i<45;i++){
			if(!List.of(10,11,19,20,28,29,24).contains(i)){
				inventory.setItem(i,item);
			}
		}
		item.setPDC("max_spell_tier", max_spell_tier);
		inventory.setItem(0,item);

		item = new Item(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ");
		for(int i=45;i<54;i++){
			if(i != 49){
				inventory.setItem(i,item);
			}
		}
		item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Close");
		inventory.setItem(49,item);

		item.setDisplayName(ChatColor.RED+"Invalid Spell");
		inventory.setItem(22,item);

		item = new Item(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(ChatColor.GRAY+"Place Wand Here");
		inventory.setItem(15,item);
		inventory.setItem(33,item);

		player.openInventory(inventory);
	}

	@EventHandler
	public void onGrimoireClose(InventoryCloseEvent event){
		if(!event.getView().getTitle().equals(ChatColor.WHITE+"Grimoire")) return;
		Inventory inventory = event.getInventory();
		for(int i : List.of(10,11,19,20,24,28,29)){
			if(inventory.getItem(i) != null){
				event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), inventory.getItem(i));
				inventory.setItem(i, new ItemStack(Material.AIR));
			}
		}
		Item item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Invalid Recipe");
		event.getInventory().setItem(22,item);
	}

	private boolean doesRecipeMatch(List<Item> items, List<Object> objectLst){
		List<Item> itemList = new ArrayList<>(items);
		List<Object> objects = new ArrayList<>(objectLst);
		if(itemList.size() != objects.size()){
			return false;
		}
		for(int i=0;i<objects.size();i++){
			if(objects.get(i) instanceof Item item){
				for(int j=0;j<itemList.size();j++){
					if(itemList.get(j).doesMatch(item)){
						itemList.remove(j);
						objects.remove(i);
						i--;
						break;
					}
				}
			}
			else if(objects.get(i) instanceof Material material){
				Item item = new Item(material);
				for(int j=0;j<itemList.size();j++){
					if(itemList.get(j).doesMatch(item)){
						itemList.remove(j);
						objects.remove(i);
						i--;
						break;
					}
				}
			}
		}
		if(!itemList.isEmpty() || !objects.isEmpty()){
			return false;
		}
		return true;
	}

	@EventHandler
	public void onGrimoireInteract(InventoryDragEvent event){
		if(!event.getView().getTitle().equals(ChatColor.WHITE+"Grimoire") || event.getInventory() == event.getWhoClicked().getInventory()) return;
		refreshRecipe(event.getInventory());
	}

	private void refreshRecipe(Inventory inventory){
		plugin.getRunnableHandler().scheduleDelayedTask(() ->{
			List<Item> items = new ArrayList<>();
			for(int i : List.of(10,11,19,20,28,29)){
				if(inventory.getItem(i) != null){
					items.add(new Item(inventory.getItem(i)));
				}
			}
			if(inventory.getItem(24) == null){
				inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"No Wand In Slot"));
				return;
			}
			Item wand = new Item(inventory.getItem(24));
			if(!wand.hasPDC("ItemID", PersistentDataType.STRING) || !wand.getPDC("ItemID", PersistentDataType.STRING).equals("wand")){
				inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"No Wand In Slot"));
				return;
			}
			int maxSpellTier = wand.getPDC("max_spell_tier", PersistentDataType.INTEGER);
			int freeSlots = wand.<Integer>getPDC("max_slots", PersistentDataType.INTEGER) -
							wand.<Integer>getPDC("used_slots", PersistentDataType.INTEGER);
			boolean recipeFound = false;
			for(String id : recipes.keySet()){
				SpellRecipe recipe = recipes.get(id);
				if(!doesRecipeMatch(items, recipe.objects)){
					continue;
				}
				//
				if(maxSpellTier < recipe.tier){
					inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Wand Quality Too Low"));
					return;
				}
				if(freeSlots < recipe.slotCost){
					inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Not Enough Available Slots"));
					return;
				}
				Item item = new Item(Material.LIME_STAINED_GLASS_PANE).setDisplayName(ChatColor.WHITE+"Add Spell To Wand");
				List<String> lore = new ArrayList<>();
				lore.add(ChatColor.GRAY+"Spell: "+recipe.spellDisplayName);
				lore.add(ChatColor.GRAY+"Slots: "+recipe.slotCost);
				lore.add(ChatColor.GRAY+"Tier: "+tierToString(recipe.tier));
				item.setLore(lore);
				inventory.setItem(22,item);
				recipeFound = true;
			}
			if(!recipeFound){
				Item item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Invalid Spell");
				inventory.setItem(22,item);
			}
		},1);
	}

	@EventHandler
	public void onGrimoireInteract(InventoryClickEvent event){
		if(event.getClickedInventory() == null || event.getSlot() == -999) return;
		if(event.getClickedInventory() == event.getWhoClicked().getInventory() || !event.getView().getTitle().equals(ChatColor.WHITE+"Grimoire")) return;
		if(event.getSlot() == 49){
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}
		if(!List.of(10,11,19,20,24,28,29).contains(event.getSlot())){
			event.setCancelled(true);
		}
		refreshRecipe(event.getClickedInventory());
		if(event.getSlot() == 22 && event.getClickedInventory().getItem(22) != null &&
				event.getClickedInventory().getItem(22).getType() == Material.LIME_STAINED_GLASS_PANE){
			Inventory inventory = event.getClickedInventory();
			List<Item> items = new ArrayList<>();
			for(int i : List.of(10,11,19,20,28,29)){
				if(inventory.getItem(i) != null){
					items.add(new Item(inventory.getItem(i)));
				}
			}
			if(inventory.getItem(24) == null){
				inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"No Wand In Slot"));
				return;
			}
			Item wand = new Item(inventory.getItem(24));
			if(!wand.hasPDC("ItemID", PersistentDataType.STRING) || !wand.getPDC("ItemID", PersistentDataType.STRING).equals("wand")){
				inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"No Wand In Slot"));
				return;
			}
			int maxSpellTier = wand.getPDC("max_spell_tier", PersistentDataType.INTEGER);
			int freeSlots = wand.<Integer>getPDC("max_slots", PersistentDataType.INTEGER) -
					wand.<Integer>getPDC("used_slots", PersistentDataType.INTEGER);

			for(String id : recipes.keySet()){
				SpellRecipe recipe = recipes.get(id);
				if(!doesRecipeMatch(items, recipe.objects)){
					continue;
				}
				//
				if(maxSpellTier < recipe.tier){
					inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Wand Quality Too Low"));
					return;
				}
				if(freeSlots < recipe.slotCost){
					inventory.setItem(22,new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Not Enough Available Slots"));
					return;
				}
				//
				ItemStack[] itemStacks = new ItemStack[6];
				for(int i=0;i<6;i++){
					itemStacks[i] = inventory.getItem(List.of(10,11,19,20,28,29).get(i));
				}
				List<Object> objects = new ArrayList<>(recipe.objects);
				for(int i=0;i<6;i++){
					if(itemStacks[i] != null) {
						for (int j = 0; j < objects.size(); j++) {
							if (objects.get(j) instanceof Item item) {
								if (new Item(itemStacks[i]).doesMatch(item)) {
									itemStacks[i].setAmount(itemStacks[i].getAmount() - item.getAmount());
									objects.remove(j);
									break;
								}
							} else if (objects.get(j) instanceof Material material) {
								if (new Item(itemStacks[i]).doesMatch(new Item(material))) {
									itemStacks[i].setAmount(itemStacks[i].getAmount() - 1);
									objects.remove(j);
									break;
								}
							}
						}
					}
				}
				//
				List<String> lore = wand.getItemMeta().getLore();
				String spellsString = wand.getPDC("spells", PersistentDataType.STRING);
				if(spellsString.equals("")){
					lore.add(recipe.spellDisplayName+" <");
				}
				else{
					lore.add(recipe.spellDisplayName);
				}

				if(!wand.getPDC("spells", PersistentDataType.STRING).equals("")){
					spellsString += ",";
				}
				spellsString += id;
				int usedSlots = wand.getPDC("used_slots", PersistentDataType.INTEGER);
				usedSlots += recipe.slotCost;
				wand.setPDC("spells", spellsString);
				wand.setPDC("used_slots", usedSlots);
				lore.set(1, ChatColor.GRAY+""+usedSlots+" / "+wand.<Integer>getPDC("max_slots", PersistentDataType.INTEGER));
				wand.setLore(lore);
				inventory.setItem(24,wand);
				refreshRecipe(inventory);
				break;
			}
		}
	}
}
