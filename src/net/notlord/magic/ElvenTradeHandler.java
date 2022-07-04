package net.notlord.magic;

import net.md_5.bungee.api.ChatColor;
import net.notlord.Main;
import net.notlord.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElvenTradeHandler implements Listener {
	private static class ElvenTradeRecipe{
		public final Item item1;
		public final Item item2;
		public final Item result;
		public final int minMoonPhase;
		public final int maxMoonPhase;

		public ElvenTradeRecipe(Item item1, Item item2, Item result, int minMoonPhase, int maxMoonPhase) {
			this.item1 = item1;
			this.item2 = item2;
			this.result = result;
			this.minMoonPhase = minMoonPhase;
			this.maxMoonPhase = maxMoonPhase;
		}
	}

	private static final Map<String, ElvenTradeRecipe> recipes = new HashMap<>();
	public static void addTradeRecipe(Item item1, Item item2, Item result){
		result.addEnchant(Enchantment.ARROW_INFINITE,1,false).addItemFlag(ItemFlag.HIDE_ENCHANTS);
		recipes.put(result.getItemIDOrMaterial(), new ElvenTradeRecipe(item1,item2,result,0,8));
	}
	public static void addTradeRecipe(Item item1, Item item2, Item result, int minMoonPhase, int maxMoonPhase){
		result.addEnchant(Enchantment.ARROW_INFINITE,1,false).addItemFlag(ItemFlag.HIDE_ENCHANTS);
		recipes.put(result.getItemIDOrMaterial(), new ElvenTradeRecipe(item1,item2,result,minMoonPhase,maxMoonPhase));
	}

	private static final Map<String, ElvenTradeRecipe> currentRecipes = new HashMap<>();

	public static void initRecipes(Main plugin){}

	private static final Map<String, Inventory> inventories = new HashMap<>();

	private volatile boolean bool = false;
	private final Main plugin;
	public ElvenTradeHandler(Main plugin){
		this.plugin = plugin;
		initRecipes(plugin);
		refreshTrades(0);
		plugin.getRunnableHandler().scheduleTickTask(() -> {
			long tick = plugin.getServer().getWorld("world").getTime();
			if(tick > 0 && bool){
				int days= (int) plugin.getServer().getWorld("world").getFullTime()/24000;
				int phase=days%8;
				refreshTrades(phase);
				bool = false;
			}
			else if(tick > 13800 && !bool){
				bool = true;
			}
		});
	}

	public static void openElvenTradeMenu(Player player){
		if(inventories.containsKey(player.getUniqueId().toString())){
			player.openInventory(inventories.get(player.getUniqueId().toString()));
			return;
		}
		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.WHITE+"Elven Trade");
		Item item = new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
		for(int i=0;i<45;i++){
			if(i == 29 || i == 30 || i == 33) continue;
			inventory.setItem(i,item);
		}
		item.setType(Material.RED_STAINED_GLASS_PANE);
		for(int i=45;i<54;i++){
			inventory.setItem(i,item);
		}
		item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Close");
		inventory.setItem(49,item);
		for(int i=0;i<5;i++){
			inventory.setItem(List.of(2,3,4,5,6).get(i), ((ElvenTradeRecipe) currentRecipes.values().toArray()[i]).result);
		}
		inventory.getItem(0).setItemMeta(new Item(inventory.getItem(0)).setPDC("current_recipe_id", "").getItemMeta());
		player.openInventory(inventory);
		inventories.put(player.getUniqueId().toString(), inventory);
	}

	private static void setCurrentInventoryRecipe(Inventory inventory, String recipeId){
		Item item;
		if(!currentRecipes.containsKey(recipeId)){
			item = new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
			inventory.setItem(20, item);
			inventory.setItem(21, item);
			inventory.setItem(24, item);
			inventory.getItem(0).setItemMeta(new Item(inventory.getItem(0)).setPDC("current_recipe_id", "").getItemMeta());
			return;
		}
		inventory.getItem(0).setItemMeta(new Item(inventory.getItem(0)).setPDC("current_recipe_id", recipeId).getItemMeta());
		ElvenTradeRecipe recipe = currentRecipes.get(recipeId);
		inventory.setItem(20, new Item(recipe.item1).setPDC("unstackable", 0));
		if(recipe.item2 != null){
			inventory.setItem(21, new Item(recipe.item2).setPDC("unstackable", 0));
		}
		else{
			inventory.setItem(21, new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" "));
		}
		inventory.setItem(24, new Item(recipe.result).setPDC("unstackable", 0));
	}

	private static void refreshTrades(int moonPhase){
		currentRecipes.clear();
		int i=0;
		for(String recipeId : recipes.keySet()){
			ElvenTradeRecipe recipe = recipes.get(recipeId);
			if(i == 5) break;
			if(moonPhase >= recipe.minMoonPhase && moonPhase <= recipe.maxMoonPhase){
				currentRecipes.put(recipeId, recipe);
				i++;
			}
		}
		for(Inventory inventory : inventories.values()){
			for(i=0;i<5;i++){
				inventory.setItem(List.of(2,3,4,5,6).get(i), ((ElvenTradeRecipe) currentRecipes.values().toArray()[i]).result);
			}
			inventory.getItem(0).setItemMeta(new Item(inventory.getItem(0)).setPDC("current_recipe_id", "").getItemMeta());
		}

	}

	private void refreshRecipe(Inventory inventory){
		plugin.getRunnableHandler().scheduleDelayedTask(() -> {
			ItemStack itemStackReq = inventory.getItem(20);
			ItemStack itemStackIn = inventory.getItem(29);
			ElvenTradeRecipe recipe = currentRecipes.get(new Item(inventory.getItem(0)).<String>getPDC("current_recipe_id", PersistentDataType.STRING));
			boolean recipeMatch = recipe != null && itemStackIn != null;
			if(itemStackIn != null && !new Item(itemStackIn).doesMatch(new Item(itemStackReq))) recipeMatch = false;
			itemStackReq = inventory.getItem(21);
			itemStackIn = inventory.getItem(30);
			if(itemStackIn != null && itemStackReq.getType() == Material.GRAY_STAINED_GLASS_PANE) recipeMatch = false;
			if(itemStackIn == null && itemStackReq.getType() != Material.GRAY_STAINED_GLASS_PANE) recipeMatch = false;
			if(itemStackIn != null && !new Item(itemStackIn).doesMatch(new Item(itemStackReq))) recipeMatch = false;

			if(recipeMatch){
				inventory.setItem(33, recipe.result);
			}
			else{
				inventory.setItem(33, new ItemStack(Material.AIR));
			}
		},1);
	}

	@EventHandler
	public void closeInventoryEvent(InventoryCloseEvent event){
		if(!event.getView().getTitle().equals(ChatColor.WHITE+"Elven Trade")) return;
		for(int i=0;i<2;i++){
			ItemStack itemStack = event.getInventory().getItem(List.of(29,30).get(i));
			if(itemStack != null){
				event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack);
				itemStack.setAmount(0);
			}
		}
		setCurrentInventoryRecipe(event.getInventory(), "");
	}
	@EventHandler
	public void inventoryClickEvent(InventoryDragEvent event){
		if(!event.getView().getTitle().equals(ChatColor.WHITE+"Elven Trade") || event.getInventory() == event.getWhoClicked().getInventory()) return;
		refreshRecipe(event.getInventory());
	}

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event){
		if(event.getClickedInventory() == null || event.getSlot() == -999) return;
		if(event.getClickedInventory() == event.getWhoClicked().getInventory() || !event.getView().getTitle().equals(ChatColor.WHITE+"Elven Trade")) return;
		if(event.getSlot() == 49){
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}
		if(!List.of(29,30).contains(event.getSlot())){
			event.setCancelled(true);
		}
		if(List.of(2,3,4,5,6).contains(event.getSlot())){
			setCurrentInventoryRecipe(event.getClickedInventory(), new Item(event.getClickedInventory().getItem(event.getSlot())).getItemIDOrMaterial());
		}
		refreshRecipe(event.getClickedInventory());
		if(event.getSlot() == 33){
			plugin.getRunnableHandler().scheduleDelayedTask(() -> {
				Inventory inventory = event.getClickedInventory();
				ItemStack itemStackReq = inventory.getItem(20);
				ItemStack itemStackIn = inventory.getItem(29);
				ElvenTradeRecipe recipe = currentRecipes.get(new Item(inventory.getItem(0)).<String>getPDC("current_recipe_id", PersistentDataType.STRING));
				boolean recipeMatch = recipe != null && itemStackIn != null;
				if(itemStackIn != null && !new Item(itemStackIn).doesMatch(new Item(itemStackReq))) recipeMatch = false;
				itemStackReq = inventory.getItem(21);
				itemStackIn = inventory.getItem(30);
				if(itemStackIn != null && itemStackReq.getType() == Material.GRAY_STAINED_GLASS_PANE) recipeMatch = false;
				if(itemStackIn != null && !new Item(itemStackIn).doesMatch(new Item(itemStackReq))) recipeMatch = false;

				if(recipeMatch){
					event.getClickedInventory().getItem(29).setAmount(event.getClickedInventory().getItem(29).getAmount() - event.getClickedInventory().getItem(20).getAmount());
					if(inventory.getItem(21).getType() != Material.GRAY_STAINED_GLASS_PANE){
						event.getClickedInventory().getItem(30).setAmount(event.getClickedInventory().getItem(30).getAmount() - event.getClickedInventory().getItem(21).getAmount());
					}
					Item item = new Item(event.getClickedInventory().getItem(24));
					item.removePDC("unstackable");
					if(item.isSimilar(event.getWhoClicked().getItemOnCursor())){
						event.getWhoClicked().getItemOnCursor().setAmount(event.getWhoClicked().getItemOnCursor().getAmount() + item.getAmount());
					}
					else {
						event.getWhoClicked().setItemOnCursor(item);
					}
				}
				refreshRecipe(event.getClickedInventory());
			},1);
		}
	}

}
