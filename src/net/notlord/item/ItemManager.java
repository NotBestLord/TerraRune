package net.notlord.item;

import net.md_5.bungee.api.ChatColor;
import net.notlord.Main;
import net.notlord.magic.ElvenTradeHandler;
import net.notlord.magic.GrimoireHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemManager {
	private final Map<String, Item> items = new HashMap<>();

	public ItemManager(Main plugin){
		ShapedRecipe shapedRecipe;
		addItem(new Item(Material.STICK).setPDC("ItemID", "GamemodeStick")
				.addEnchant(Enchantment.ARROW_INFINITE, 1, false).setDisplayName(ChatColor.GOLD+"Gamemode Stick").addItemFlag(ItemFlag.HIDE_ENCHANTS));

		ItemAbilityHandler.addEvent(PlayerInteractEvent.class, "GamemodeStick", e -> {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				switch (event.getPlayer().getGameMode()) {
					case CREATIVE -> event.getPlayer().setGameMode(GameMode.SURVIVAL);
					case SURVIVAL -> event.getPlayer().setGameMode(GameMode.CREATIVE);
				}
			}
		});

		addItem(new Item(Material.LEATHER).setPDC("ItemID", "hoglin_hide").setDisplayName(ChatColor.WHITE+"Hoglin Hide"));



		addItem(new Item(Material.PAPER).setPDC("ItemID", "mangrove_paper").setDisplayName(ChatColor.WHITE+"Mangrove Paper"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "mangrove_paper"), getItem("mangrove_paper").setItemAmount(2));
		shapedRecipe.shape("aaa");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.MANGROVE_LOG, 24)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.PAPER).setPDC("ItemID", "echo_parchment").setDisplayName(ChatColor.WHITE+"Echo Parchment"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "echo_parchment"), getItem("echo_parchment").setItemAmount(3));
		shapedRecipe.shape("aaa","bbb","aaa");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.ECHO_SHARD, 1)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(getItem("mangrove_paper").setItemAmount(4)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.PAPER).setPDC("ItemID", "sculk_sheet").setDisplayName(ChatColor.WHITE+"Sculk Sheet"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "sculk_sheet"), getItem("sculk_sheet").setItemAmount(4));
		shapedRecipe.shape("cac","bbb","cac");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.SCULK, 32)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(getItem("echo_parchment").setItemAmount(2)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(new ItemStack(Material.POPPED_CHORUS_FRUIT, 16)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STRING).setUnplacable().setPDC("ItemID", "blaze_rope").setDisplayName(ChatColor.WHITE+"Blaze Rope"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "blaze_rope"), getItem("blaze_rope").setItemAmount(4));
		shapedRecipe.shape("aba","b b","aba");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.BLAZE_POWDER, 2)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.BLAZE_ROD, 1)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STRING).setUnplacable().setPDC("ItemID", "chorus_thread").setDisplayName(ChatColor.WHITE+"Chorus Thread"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "chorus_thread"), getItem("chorus_thread").setItemAmount(4));
		shapedRecipe.shape("aba","bcb","aba");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.CHORUS_FRUIT, 16)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.CHORUS_FLOWER, 4)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(getItem("blaze_rope").setItemAmount(2)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STRING).setUnplacable().setPDC("ItemID", "netherite_wire").setDisplayName(ChatColor.WHITE+"Netherite Wire"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "netherite_wire"), getItem("netherite_wire").setItemAmount(4));
		shapedRecipe.shape("aba","bcb","aba");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.IRON_BLOCK, 2)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.NETHERITE_INGOT, 1)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(getItem("chorus_thread").setItemAmount(4)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.NETHERITE_SWORD).setHolo().setPDC("ItemID", "holo_sword").setDisplayName(ChatColor.WHITE+"Holo Sword"));

		addItem(new Item(Material.ENCHANTED_BOOK).setPDC("ItemID", "grimoire").setDisplayName(ChatColor.WHITE+"Grimoire"));

		ItemAbilityHandler.addEvent(PlayerInteractEvent.class, "grimoire", e -> {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				GrimoireHandler.openGrimoire(event.getPlayer(), new Item(event.getItem()).getPDC("max_spell_tier", PersistentDataType.INTEGER));
			}
		});



		addItem(new Item(Material.ENCHANTED_BOOK).setPDC("ItemID", "grimoire_smelt").setDisplayName(ChatColor.WHITE+"Grimoire-Smelt").setPDC("spell", "smelt"));

		Map<String, Integer> map1 = new HashMap<>();
		Map<String, Integer> map2 = new HashMap<>();
		map1.put("paper", 0);
		map1.put("leather", 0);
		map1.put("string", 0);
		map2.put("paper", 5);
		map2.put("leather", 12);
		map2.put("string", 8);
		//
		map1.put("mangrove_paper", 5);
		map1.put("phantom_membrane", 12);
		map1.put("blaze_rope", 8);
		map2.put("mangrove_paper", 12);
		map2.put("phantom_membrane", 22);
		map2.put("blaze_rope", 16);
		//
		map1.put("echo_parchment", 12);
		map1.put("hoglin_hide", 22);
		map1.put("chorus_thread", 16);
		map2.put("echo_parchment", 24);
		map2.put("hoglin_hide", 27);
		map2.put("chorus_thread", 24);
		//
		map1.put("sculk_sheet", 24);
		map1.put("shulker_shell", 27);
		map1.put("netherite_wire", 24);
		map2.put("sculk_sheet", 30);
		map2.put("shulker_shell", 37);
		map2.put("netherite_wire", 33);
		//
		Item item = new Item(Material.ENCHANTED_BOOK).setPDC("ItemID", "mystery_grimoire").setDisplayName(ChatColor.WHITE+"Mystery Grimoire");
		int i=0;
		for(Item paper : List.of(new Item(Material.PAPER, 9), getItem("mangrove_paper").setItemAmount(9),
				getItem("echo_parchment").setItemAmount(9), getItem("sculk_sheet").setItemAmount(9))){
			for(Item leather : List.of(new Item(Material.LEATHER, 3), getItem("hoglin_hide").setItemAmount(3),
					new Item(Material.SHULKER_SHELL, 3), new Item(Material.PHANTOM_MEMBRANE, 3))){
				for(Item string : List.of(new Item(Material.STRING, 4), getItem("blaze_rope").setItemAmount(4),
						getItem("chorus_thread").setItemAmount(4), getItem("netherite_wire").setItemAmount(4))){
					int minQuality = map1.get(paper.getItemIDOrMaterial()) + map1.get(leather.getItemIDOrMaterial()) + map1.get(string.getItemIDOrMaterial());
					int maxQuality = map2.get(paper.getItemIDOrMaterial()) + map2.get(leather.getItemIDOrMaterial()) + map2.get(string.getItemIDOrMaterial());
					shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "mystery_grimoire_"+i),
							new Item(item).setPDC("min_quality", minQuality).setPDC("max_quality", maxQuality)
									.setLore(new ArrayList<>(List.of(ChatColor.GRAY+"When used, will turn into a grimoire",
											ChatColor.GRAY+"with a quality of "+minQuality+"%-"+maxQuality+"%"))));
					shapedRecipe.shape("baa","bcc","baa");
					shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(leather));
					shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(string));
					shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(paper));
					plugin.getServer().addRecipe(shapedRecipe);
					i++;
				}
			}
		}

		ItemAbilityHandler.addEvent(PlayerInteractEvent.class, "mystery_grimoire", e -> {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Item item1 = new Item(event.getItem());
				Item item2 = getItem("grimoire");
				int quality = new Random().nextInt((int) (0.5f + Math.random() +
						item1.<Integer>getPDC("max_quality", PersistentDataType.INTEGER) - item1.<Integer>getPDC("min_quality", PersistentDataType.INTEGER))) +
						item1.<Integer>getPDC("min_quality", PersistentDataType.INTEGER);
				item2.setPDC("quality", quality);
				if(quality < 25){
					item2.setPDC("max_spell_tier", 1);
				}
				else if(quality < 50){
					item2.setPDC("max_spell_tier", 2);
				}
				else if(quality < 75){
					item2.setPDC("max_spell_tier", 3);
				}
				else if(quality < 95){
					item2.setPDC("max_spell_tier", 4);
				}else{
					item2.setPDC("max_spell_tier", 5);
				}
				item2.setLore(new ArrayList<>(List.of(ChatColor.GRAY+"Quality : " + quality + "%")));
				event.getItem().setAmount(event.getItem().getAmount()-1);
				plugin.getRunnableHandler().scheduleDelayedTask(()-> event.getPlayer().getInventory().addItem(item2),1);
			}
		});

		addItem(new Item(Material.EMERALD).setPDC("ItemID", "soul_emerald").setDisplayName(ChatColor.WHITE+"Soul Emerald"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "soul_emerald"), getItem("soul_emerald").setItemAmount(2));
		shapedRecipe.shape(" a ","aba"," a ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.EMERALD, 8)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.SOUL_SAND, 64)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.DIAMOND).setPDC("ItemID", "soul_diamond").setDisplayName(ChatColor.WHITE+"Soul Diamond"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "soul_diamond"), getItem("soul_diamond").setItemAmount(2));
		shapedRecipe.shape(" a ","aba"," a ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.DIAMOND, 4)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.SOUL_SAND, 64)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.AMETHYST_SHARD).setPDC("ItemID", "conductive_amethyst_shard").setDisplayName(ChatColor.WHITE+"Conductive Amethyst Shard"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "conductive_amethyst_shard"), getItem("conductive_amethyst_shard").setItemAmount(2));
		shapedRecipe.shape(" c ","bab"," c ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.AMETHYST_SHARD, 24)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.SOUL_SAND, 64)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(new ItemStack(Material.REDSTONE_BLOCK, 8)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.ECHO_SHARD).setPDC("ItemID", "sculk_sensor_core").setDisplayName(ChatColor.WHITE+"Sculk Sensor Core"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "sculk_sensor_core"), getItem("sculk_sensor_core").setItemAmount(2));
		shapedRecipe.shape(" a ","bcb"," a ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.ECHO_SHARD, 8)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_INGOT, 32)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(new ItemStack(Material.SCULK_SENSOR,16)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STICK).setPDC("ItemID", "iron_rod").setDisplayName(ChatColor.WHITE+"Iron Rod"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "iron_rod"), getItem("iron_rod"));
		shapedRecipe.shape("a","a","a");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.IRON_BLOCK, 2)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STICK).setPDC("ItemID", "copper_rod").setDisplayName(ChatColor.WHITE+"Copper Rod"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "copper_rod"), getItem("copper_rod"));
		shapedRecipe.shape("a","a","a");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.COPPER_BLOCK, 3)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STICK).setPDC("ItemID", "netherite_rod").setDisplayName(ChatColor.WHITE+"Netherite Rod"));
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "netherite_rod"), getItem("netherite_rod").setItemAmount(2));
		shapedRecipe.shape("a","a","a");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.NETHERITE_INGOT, 1)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.SOUL_LANTERN).setPDC("ItemID", "void_lantern").setDisplayName(ChatColor.WHITE+"Void Lantern").setUnplacable());
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "void_lantern"), getItem("void_lantern"));
		shapedRecipe.shape(" a ","cbc"," a ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.ECHO_SHARD, 1)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.SOUL_LANTERN, 4)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(new ItemStack(Material.ENDER_PEARL, 8)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.LANTERN).setPDC("ItemID", "netherite_lamp").setDisplayName(ChatColor.WHITE+"Netherite Lamp").setUnplacable());
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "netherite_lamp"), getItem("netherite_lamp").setItemAmount(4));
		shapedRecipe.shape(" a ","aba"," a ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.NETHERITE_INGOT, 2)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(getItem("void_lantern").setItemAmount(4)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.RED_STAINED_GLASS).setPDC("ItemID", "nether_core").setDisplayName(ChatColor.WHITE+"Nether Core").setUnplacable());
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "nether_core"), getItem("nether_core").setItemAmount(2));
		shapedRecipe.shape("cac", "aba","cac");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.NETHER_BRICKS,64)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.NETHER_STAR, 1)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_BLOCK, 32)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.YELLOW_STAINED_GLASS).setPDC("ItemID", "end_core").setDisplayName(ChatColor.WHITE+"End Core").setUnplacable());
		shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "end_core"), getItem("end_core").setItemAmount(2));
		shapedRecipe.shape("cac", "aba","cac");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(new ItemStack(Material.END_STONE,64)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.NETHER_STAR, 2)));
		shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(new ItemStack(Material.PURPUR_BLOCK, 16)));
		plugin.getServer().addRecipe(shapedRecipe);

		addItem(new Item(Material.STICK).setPDC("ItemID", "wand").setDisplayName(ChatColor.WHITE+"Wand").
				addEnchant(Enchantment.ARROW_INFINITE, 1,false).addItemFlag(ItemFlag.HIDE_ENCHANTS));

		//
		map1 = new HashMap<>();
		map2 = new HashMap<>();
		//
		map1.put("stick", 0);
		map1.put("bone", 2);
		map1.put(Material.JACK_O_LANTERN.name().toLowerCase(), 2);
		map1.put("lantern", 0);
		map1.put("diamond", 2);
		map1.put("emerald", 0);

		map2.put("stick", 5);
		map2.put("bone", 4);
		map2.put(Material.JACK_O_LANTERN.name().toLowerCase(), 10);
		map2.put("lantern", 12);
		map2.put("diamond", 6);
		map2.put("emerald", 8);
		//
		map1.put("iron_rod", 5);
		map1.put("copper_rod", 7);
		map1.put("glowstone", 12);
		map1.put("soul_lantern", 14);
		map1.put("echo_shard", 8);
		map1.put("amethyst_shard", 10);


		map2.put("iron_rod", 12);
		map2.put("copper_rod", 10);
		map2.put("glowstone", 22);
		map2.put("soul_lantern", 20);
		map2.put("echo_shard", 16);
		map2.put("amethyst_shard", 14);
		//
		map1.put("blaze_rod", 15);
		map1.put("end_rod", 12);
		map1.put("void_lantern", 24);
		map1.put("nether_core", 22);
		map1.put("soul_emerald", 16);
		map1.put("soul_diamond", 18);

		map2.put("blaze_rod", 21);
		map2.put("end_rod", 24);
		map2.put("void_lantern", 25);
		map2.put("nether_core", 27);
		map2.put("soul_emerald", 24);
		map2.put("soul_diamond", 22);
		//
		map1.put("netherite_rod", 24);
		map1.put("netherite_lamp", 30);
		map1.put("end_core", 27);
		map1.put("conductive_amethyst_shard", 24);
		map1.put("sculk_sensor_core", 27);

		map2.put("netherite_rod", 30);
		map2.put("netherite_lamp", 33);
		map2.put("end_core", 37);
		map2.put("conductive_amethyst_shard", 33);
		map2.put("sculk_sensor_core", 30);
		//

		//
		item = new Item(Material.STICK).setPDC("ItemID", "mystery_wand").setDisplayName(ChatColor.WHITE+"Mystery Wand")
				.addEnchant(Enchantment.ARROW_INFINITE, 1, false).addItemFlag(ItemFlag.HIDE_ENCHANTS).setUnplacable();
		i=0;
		for(String stickId : List.of("stick", "bone", "iron_rod", "copper_rod","blaze_rod","end_rod","netherite_rod")){
			Item stick = getItem(stickId);
			if(stick == null){
				stick = new Item(Material.valueOf(stickId.toUpperCase()));
			}
			stick.setItemAmount(3);
			for(String sourceId : List.of("lantern",Material.JACK_O_LANTERN.name().toLowerCase(),"glowstone","soul_lantern","void_lantern",
					"nether_core","netherite_lamp","end_core")){
				Item source = getItem(sourceId);
				if(source == null){
					source = new Item(Material.valueOf(sourceId.toUpperCase()));
				}
				source.setItemAmount(4);
				for(String conductorId : List.of("emerald","diamond","echo_shard","amethyst_shard","soul_emerald","soul_diamond",
						"conductive_amethyst_shard", "sculk_sensor_core")){
					Item conductor = getItem(conductorId);
					if(conductor == null){
						conductor = new Item(Material.valueOf(conductorId.toUpperCase()));
					}
					conductor.setItemAmount(4);
					//
					int minQuality = map1.get(stickId) + map1.get(sourceId) + map1.get(conductorId);
					int maxQuality = map2.get(stickId) + map2.get(sourceId) + map2.get(conductorId);
					Item item1 = new Item(item);
					item1.setType(stick.getType());
					shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "mystery_wand_"+i),
							item1.setPDC("min_quality", minQuality).setPDC("max_quality", maxQuality)
									.setLore(new ArrayList<>(List.of(ChatColor.GRAY+"When used, will turn into a wand",
											ChatColor.GRAY+"with a quality of "+minQuality+"%-"+maxQuality+"%"))));
					shapedRecipe.shape("a","b","c");
					shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(conductor));
					shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(stick));
					shapedRecipe.setIngredient('c', new RecipeChoice.ExactChoice(source));
					plugin.getServer().addRecipe(shapedRecipe);
					i++;
				}
			}
		}

		ItemAbilityHandler.addEvent(PlayerInteractEvent.class, "mystery_wand", e -> {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Item item1 = new Item(event.getItem());
				Item item2 = getItem("wand");
				item2.setType(item1.getType());
				int quality = new Random().nextInt((int) (0.5f + Math.random() +
						item1.<Integer>getPDC("max_quality", PersistentDataType.INTEGER) - item1.<Integer>getPDC("min_quality", PersistentDataType.INTEGER))) +
						item1.<Integer>getPDC("min_quality", PersistentDataType.INTEGER);
				item2.setPDC("quality", quality);
				item2.setPDC("used_slots", 0);
				item2.setPDC("level", 1);
				item2.setPDC("exp", 0);
				item2.setPDC("req_exp", 500);
				item2.setPDC("spells", "");
				item2.setPDC("current_spell", 0);
				if(quality < 25){
					item2.setPDC("max_slots", 1);
					item2.setPDC("max_spell_tier", 1);
				}
				else if(quality < 50){
					item2.setPDC("max_slots", 2);
					item2.setPDC("max_spell_tier", 2);
				}
				else if(quality < 75){
					item2.setPDC("max_slots", 2);
					item2.setPDC("max_spell_tier", 3);
				}
				else if(quality < 95){
					item2.setPDC("max_slots", 2);
					item2.setPDC("max_spell_tier", 4);
				}
				else if(quality < 100){
					item2.setPDC("max_slots", 3);
					item2.setPDC("max_spell_tier", 5);
				}
				else {
					item2.setPDC("max_slots", 4);
					item2.setPDC("max_spell_tier", 5);
				}
				item2.setLore(new ArrayList<>(List.of(ChatColor.GRAY+"Quality : " + quality + "%",
													  ChatColor.GRAY+"Slots: " + 0 + " / " + item2.<Integer>getPDC("max_slots", PersistentDataType.INTEGER),
													  ChatColor.GRAY+"Level: 1",
						                              ChatColor.GRAY+"Spells:")));
				event.getItem().setAmount(event.getItem().getAmount()-1);
				plugin.getRunnableHandler().scheduleDelayedTask(()-> event.getPlayer().getInventory().addItem(item2),1);
			}
		});

		ItemAbilityHandler.addEvent(PlayerInteractEvent.class, "wand", e -> {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				event.setCancelled(true);
				Item item1 = new Item(event.getItem());
				if(item1.getPDC("spells", PersistentDataType.STRING).equals("")) return;
				List<String> lore = item1.getItemMeta().getLore();
				int currentSpell = item1.getPDC("current_spell", PersistentDataType.INTEGER);
				lore.set(4+currentSpell, lore.get(4+currentSpell).substring(0,lore.get(4+currentSpell).length()-2));

				currentSpell++;
				if(currentSpell >= item1.<String>getPDC("spells", PersistentDataType.STRING).split(",").length){
					currentSpell = 0;
				}
				item1.setPDC("current_spell", currentSpell);
				lore.set(4+currentSpell, lore.get(4+currentSpell) + " <");
				item1.setLore(lore);
				event.getItem().setItemMeta(item1.getItemMeta());
			}
		});

		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JlOGFkMzgyNzc5YmE2ZjIwZDIwZDUyZTcyYWU2NTY3MTAzNzcxYTdiOWFjZTFiZmYxMGY5MGU1ZGRmOTc2YSJ9fX0")
				.setDisplayName(ChatColor.GREEN+"Elven Trade Portal").setPDC("ItemID", "elven_trade_portal").setUnplacable().setLore(List.of(
						ChatColor.GRAY+"Use on a villager to open trade menu.",
						ChatColor.GRAY+"Recipes refresh every day."
				)));
		ItemAbilityHandler.addEvent(PlayerInteractAtEntityEvent.class, "elven_trade_portal", e -> {
			PlayerInteractAtEntityEvent event = (PlayerInteractAtEntityEvent) e;
			if(event.getRightClicked() instanceof Villager){
				ElvenTradeHandler.openElvenTradeMenu(event.getPlayer());
			}
		});

		// elven items
		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkxMjY2OTc5MDNmNTk0NzMzM2MwNDRiNWUzOTQ2NTUxOGQ4M2M0MjhlMGVkZjQ0MTRiOGNmNmZiODFhNDgwOSJ9fX0")
				.setDisplayName(ChatColor.WHITE+"Mythril Ingot").setUnplacable().setPDC("ItemID", "mythril_ingot"));

		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNlYzU5NWU2MzMwOWJiNTA1NjhjOWMyM2M5YjVlYjFkZjdjMmJiMzM5OTExYzQxOTY0MTIyYmYzYzEzMzRjYiJ9fX0")
				.setDisplayName(ChatColor.WHITE+"Adamantite Ingot").setUnplacable().setPDC("ItemID", "adamantite_ingot"));

		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2YwNTI5ZDAzM2Y5MzdkNGU0ZDM0ZDg5OTRmYzQ0NjU4MGQ3MDc4YWVjZWIyYzc5N2NlNGIwMWI2MDQ0ZjAzMiJ9fX0")
				.setDisplayName(ChatColor.WHITE+"Ruby").setUnplacable().setPDC("ItemID", "ruby"));

		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJhMzM4MzM0ZWM0YzA0YTMwNGEwODNhMGEwNTY5NWQ1MDM4ZWVmNmE1ZmFkZjBmZGQ2YjZlMWQ5YzM0MDIzNSJ9fX0")
				.setDisplayName(ChatColor.WHITE+"Sapphire").setUnplacable().setPDC("ItemID", "sapphire"));

		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNlYzU5NWU2MzMwOWJiNTA1NjhjOWMyM2M5YjVlYjFkZjdjMmJiMzM5OTExYzQxOTY0MTIyYmYzYzEzMzRjYiJ9fX0")
				.setDisplayName(ChatColor.WHITE+"Adamantite Ingot").setUnplacable().setPDC("ItemID", "adamantite_ingot"));

		addItem(Item.generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNlYzU5NWU2MzMwOWJiNTA1NjhjOWMyM2M5YjVlYjFkZjdjMmJiMzM5OTExYzQxOTY0MTIyYmYzYzEzMzRjYiJ9fX0")
				.setDisplayName(ChatColor.WHITE+"Adamantite Ingot").setUnplacable().setPDC("ItemID", "adamantite_ingot"));
		ElvenTradeHandler.addTradeRecipe(new Item(Material.IRON_INGOT, 32), new Item(Material.DIAMOND, 8), getItem("mythril_ingot"));
		ElvenTradeHandler.addTradeRecipe(new Item(Material.IRON_INGOT, 32), new Item(Material.DIAMOND, 8), new Item(Material.DIAMOND));
		ElvenTradeHandler.addTradeRecipe(new Item(Material.IRON_INGOT, 32), new Item(Material.DIAMOND, 8), new Item(Material.GOLD_INGOT));
		ElvenTradeHandler.addTradeRecipe(new Item(Material.IRON_INGOT, 32), new Item(Material.DIAMOND, 8), new Item(Material.IRON_INGOT));
		ElvenTradeHandler.addTradeRecipe(new Item(Material.IRON_INGOT, 32), new Item(Material.DIAMOND, 8), new Item(Material.EMERALD));
	}

	public void addItem(Item item){
		items.put(((String) item.getPDC("ItemID", PersistentDataType.STRING)).toLowerCase(), item);
	}

	public Item getItem(String itemID){
		if(items.containsKey(itemID.toLowerCase())) {
			return new Item(items.get(itemID.toLowerCase()));
		}
		return null;
	}
}
