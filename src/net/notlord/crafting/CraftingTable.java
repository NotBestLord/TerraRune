package net.notlord.crafting;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.md_5.bungee.api.ChatColor;
import net.notlord.Main;
import net.notlord.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CraftingTable implements Listener {
	private record ORecipe(Object[] objects, Class<?> recipeClass, Item result) {
		public Item getResult() {
			return result;
		}
	}
	private final Multimap<String, ORecipe> recipes = MultimapBuilder.hashKeys().linkedListValues().build();
	private final Map<String, Inventory> inventories = new HashMap<>();
	private final Main plugin;

	public CraftingTable(Main plugin) {
		this.plugin = plugin;
		Iterator<Recipe> recipeIterator = plugin.getServer().recipeIterator();
		while(recipeIterator.hasNext()){
			Recipe recipe = recipeIterator.next();
			if(recipe instanceof ShapedRecipe shapedRecipe){
				ORecipe oRecipe = new ORecipe(new Object[9], ShapedRecipe.class, new Item(shapedRecipe.getResult()));
				for(int i=0;i<9;i++){
					if(i/3 < shapedRecipe.getShape().length && i%3 < shapedRecipe.getShape()[i/3].length()){
						Character character = shapedRecipe.getShape()[i/3].charAt(i%3);
						if(shapedRecipe.getChoiceMap().containsKey(character)){
							if(shapedRecipe.getChoiceMap().get(character) == null){
								oRecipe.objects[i] = new Item(Material.AIR);
							}
							else{
								oRecipe.objects[i] = shapedRecipe.getChoiceMap().get(character);
							}
						}
						else if(shapedRecipe.getIngredientMap().containsKey(character)){
							if(shapedRecipe.getIngredientMap().get(character) == null){
								oRecipe.objects[i] = new Item(Material.AIR);
							}
							else{
								oRecipe.objects[i] = new Item(shapedRecipe.getIngredientMap().get(character));
							}
						}
						else{
							oRecipe.objects[i] = null;
						}
					}
					else{
						oRecipe.objects[i] = null;
					}
				}
				Item item = new Item(recipe.getResult());
				recipes.put(item.hasPDC("ItemID", PersistentDataType.STRING) ?
						item.getPDC("ItemID", PersistentDataType.STRING) : item.getType().toString(), oRecipe);
			}
			else if(recipe instanceof ShapelessRecipe shapelessRecipe){
				ORecipe oRecipe = new ORecipe(new Object[9], ShapelessRecipe.class, new Item(shapelessRecipe.getResult()));
				for(int i=0; i<shapelessRecipe.getChoiceList().size();i++){
					oRecipe.objects[i] = shapelessRecipe.getChoiceList().get(i);
				}
				Item item = new Item(recipe.getResult());
				recipes.put(item.hasPDC("ItemID", PersistentDataType.STRING) ?
						item.getPDC("ItemID", PersistentDataType.STRING) : item.getType().toString(), oRecipe);
			}
		}
	}

	private void generateInventory(String uuid){
		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.WHITE+"Crafting Table");
		Item item = new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
		for(int i=0; i<45;i++){
			if(!List.of(12,13,14,21,22,23,30,31,32).contains(i)){
				inventory.setItem(i, item);
			}
		}
		item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Invalid Recipe");
		inventory.setItem(25,item);
		item.setDisplayName(ChatColor.RED+"Close");
		inventory.setItem(49,item);
		item = new Item(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ");
		for(int i=45; i<54;i++){
			if(i != 49){
				inventory.setItem(i, item);
			}
		}
		inventories.put(uuid, inventory);
	}

	private boolean doesRecipeMatch(Item[] itemArr, ORecipe oRecipe){
		if(oRecipe.recipeClass == ShapedRecipe.class){
			boolean match = true;
			for(int i=0;i<9;i++){
				if(oRecipe.objects[i] instanceof Item item1) {
					if(!((itemArr[i] == null && item1.getType() == Material.AIR) || (itemArr[i] != null && itemArr[i].doesMatch(item1)))){
						match = false;
						break;
					}
				}
				else if(oRecipe.objects[i] instanceof RecipeChoice recipeChoice){
					if(itemArr[i] == null ||
							(recipeChoice instanceof RecipeChoice.ExactChoice exactChoice && !itemArr[i].doesMatch(new Item(exactChoice.getItemStack()))) ||
							(recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice &&
									(!materialChoice.getChoices().contains(itemArr[i].getType()) || itemArr[i].hasPDC("ItemID", PersistentDataType.STRING)))){
						match = false;
						break;
					}
				}
				else{
					if(itemArr[i] != null){
						match = false;
						break;
					}
				}
			}
			if(!match){
				return false;
			}
		}
		else if(oRecipe.recipeClass == ShapelessRecipe.class) {
			List<Item> itemArrayList = new ArrayList<>();
			for(Item item1 : itemArr){
				if(item1 != null) {
					itemArrayList.add(item1);
				}
			}

			List<RecipeChoice> choices = new ArrayList<>();
			for(Object object : oRecipe.objects){
				if(object instanceof RecipeChoice){
					choices.add((RecipeChoice) object);
				}
			}

			for(int i=0;i<itemArrayList.size();i++){
				for(int j=0;j<choices.size();j++){
					if(choices.get(j) instanceof RecipeChoice.ExactChoice exactChoice){
						if(itemArrayList.get(i).doesMatch(new Item(exactChoice.getItemStack()))){
							itemArrayList.remove(i);
							choices.remove(j);
							i--;
							break;
						}
					}
					else if(choices.get(j) instanceof RecipeChoice.MaterialChoice materialChoice){
						if(itemArrayList.get(i).doesMatch(new Item(materialChoice.getItemStack()))){
							itemArrayList.remove(i);
							choices.remove(j);
							i--;
							break;
						}
					}

				}
			}
			if(!itemArrayList.isEmpty() || !choices.isEmpty()){
				return false;
			}
		}
		return true;
	}

	private void reloadCurrentRecipe(Inventory inventory){
		plugin.getRunnableHandler().scheduleDelayedTask(() -> {
			Item[] itemArr = new Item[9];
			for(int i=0;i<9;i++){
				ItemStack itemStack = inventory.getItem(List.of(12,13,14,21,22,23,30,31,32).get(i));
				if(itemStack != null) {
					itemArr[i] = new Item(itemStack);
				}
				else{
					itemArr[i] = null;
				}
			}
			int con = 0;
			while(con < itemArr.length && itemArr[con] == null){
				con++;
			}
			Item[] tempArr = new Item[9];
			for(int i=0;i<itemArr.length-con;i++){
				tempArr[i] = itemArr[i+con];
			}
			ItemStack result = null;
			for(ORecipe oRecipe : recipes.values()){
				if(doesRecipeMatch(itemArr, oRecipe) || doesRecipeMatch(tempArr, oRecipe)) {
					result = oRecipe.getResult();
					break;
				}
			}
			if(result != null){
				inventory.setItem(25, result);
			}
			else{
				Item item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Invalid Recipe");
				inventory.setItem(25,item);
			}

		}, 1);
	}

	@EventHandler
	public void onCraftingTableClose(InventoryCloseEvent event){
		if(!event.getView().getTitle().equals(ChatColor.WHITE+"Crafting Table")) return;
		for(int i : List.of(12,13,14,21,22,23,30,31,32)){
			if(event.getInventory().getItem(i) != null){
				event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), event.getInventory().getItem(i));
			}
		}
		for(int i : List.of(12,13,14,21,22,23,30,31,32)){
			event.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
		Item item = new Item(Material.BARRIER).setDisplayName(ChatColor.RED+"Invalid Recipe");
		event.getInventory().setItem(25,item);
	}

	@EventHandler
	public void onCraftingTableInteract2(InventoryDragEvent event){
		if(event.getInventory() == event.getWhoClicked().getInventory() || !event.getView().getTitle().equals(ChatColor.WHITE+"Crafting Table")) return;
		reloadCurrentRecipe(event.getInventory());
	}
	@EventHandler
	public void onCraftingTableInteract(InventoryClickEvent event){
		if(event.getClickedInventory() == null || event.getSlot() == -999) return;
		if(event.getClickedInventory() == event.getWhoClicked().getInventory() || !event.getView().getTitle().equals(ChatColor.WHITE+"Crafting Table")) return;
		if(event.getSlot() == 49){
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}

		if(!List.of(12,13,14,21,22,23,30,31,32).contains(event.getSlot())){
			event.setCancelled(true);
		}

		if(event.getSlot() == 25 && event.getClickedInventory().getItem(25) != null && event.getClickedInventory().getItem(25).getType() != Material.BARRIER){
			Item item = new Item(event.getClickedInventory().getItem(25));
			if(event.getWhoClicked().getItemOnCursor().getType() == Material.AIR ||
					(item.isSimilar(event.getWhoClicked().getItemOnCursor()) &&
							item.getAmount() + event.getWhoClicked().getItemOnCursor().getAmount() <= item.getMaxStackSize())) {
				String recipeID = item.hasPDC("ItemID", PersistentDataType.STRING) ? item.getPDC("ItemID", PersistentDataType.STRING) : item.getType().toString();
				//
				Item[] itemArr = new Item[9];
				for(int i=0;i<9;i++){
					if(event.getClickedInventory().getItem(List.of(12,13,14,21,22,23,30,31,32).get(i)) != null){
						itemArr[i] = new Item(event.getClickedInventory().getItem(List.of(12,13,14,21,22,23,30,31,32).get(i)));
					}
				}
				int con = 0;
				while(con < itemArr.length && itemArr[con] == null){
					con++;
				}
				Item[] tempArr = new Item[9];
				for(int i=0;i<itemArr.length-con;i++){
					tempArr[i] = itemArr[i+con];
				}
				//
				List<Integer> ints = List.of(12,13,14,21,22,23,30,31,32);
				//
				if(recipes.containsKey(recipeID)){
					for(ORecipe recipe : recipes.get(recipeID)){
						if(doesRecipeMatch(itemArr, recipe) || doesRecipeMatch(tempArr, recipe)){
							if(recipe.recipeClass == ShapedRecipe.class){
								for(int i=0;i<recipe.objects.length;i++){
									if(recipe.objects[i] instanceof RecipeChoice.ExactChoice exactChoice){
										if(event.getClickedInventory().getItem(ints.get(i)) != null) {
											event.getClickedInventory().getItem(ints.get(i)).setAmount(
													event.getClickedInventory().getItem(ints.get(i)).getAmount() - exactChoice.getItemStack().getAmount());
										}
									}
									else if(recipe.objects[i] instanceof Item item1){
										if(event.getClickedInventory().getItem(ints.get(i)) != null) {
											event.getClickedInventory().getItem(ints.get(i)).setAmount(
													event.getClickedInventory().getItem(ints.get(i)).getAmount() - item1.getAmount());
										}
									}
									else if(event.getClickedInventory().getItem(ints.get(i)) != null){
										event.getClickedInventory().getItem(ints.get(i)).setAmount(event.getClickedInventory().getItem(ints.get(i)).getAmount()-1);
									}
								}
							}
							else if(recipe.recipeClass == ShapelessRecipe.class){
								Object[] objects = new Object[9];
								for(int i=0;i<9;i++){
									objects[i] = recipe.objects[i];
								}
								for(int i=0;i<9;i++){
									if(event.getClickedInventory().getItem(ints.get(i)) != null){
										for(int j=0;j<9;j++){
											if(objects[j] instanceof RecipeChoice.ExactChoice exactChoice){
												if(new Item(event.getClickedInventory().getItem(ints.get(i))).doesMatch(new Item(exactChoice.getItemStack()))){
													event.getClickedInventory().getItem(ints.get(i)).setAmount(
															event.getClickedInventory().getItem(ints.get(i)).getAmount() - exactChoice.getItemStack().getAmount());
													objects[j] = null;
													break;
												}
											}
											else if(objects[j] instanceof RecipeChoice.MaterialChoice materialChoice){
												if(new Item(event.getClickedInventory().getItem(ints.get(i))).doesMatch(new Item(materialChoice.getItemStack()))){
													event.getClickedInventory().getItem(ints.get(i)).setAmount(
															event.getClickedInventory().getItem(ints.get(i)).getAmount() - materialChoice.getItemStack().getAmount());
													objects[j] = null;
													break;
												}
											}
										}
									}
								}
							}
							break;
						}
					}
				}
				//
				if(item.isSimilar(event.getWhoClicked().getItemOnCursor())){
					event.getWhoClicked().getItemOnCursor().setAmount(event.getWhoClicked().getItemOnCursor().getAmount() + item.getAmount());
				}
				else {
					event.getWhoClicked().setItemOnCursor(item);
				}
				reloadCurrentRecipe(event.getClickedInventory());
			}
		}
		else {
			reloadCurrentRecipe(event.getClickedInventory());
		}
	}
	@EventHandler
	public void onCraftingTableOpen(InventoryOpenEvent event){
		if(event.getInventory().getType() != InventoryType.WORKBENCH) return;
		if(!inventories.containsKey(event.getPlayer().getUniqueId().toString())){
			generateInventory(event.getPlayer().getUniqueId().toString());
		}
		event.setCancelled(true);
		event.getPlayer().openInventory(inventories.get(event.getPlayer().getUniqueId().toString()));
	}
}
