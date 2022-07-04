package net.notlord.magic;

import net.md_5.bungee.api.ChatColor;
import net.notlord.item.Item;
import net.notlord.item.ItemManager;
import net.notlord.utils.IntStr;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EssenceHandler implements Listener {
	private final ItemManager itemManager;
	private final Map<EntityType, List<IntStr>> essenceDropList = new HashMap<>();

	public EssenceHandler(ItemManager itemManager){
		this.itemManager = itemManager;
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE0OTMxODllZmZjYjEzZGVjMDRjYjRkMzdlODQwM2MwN2RjZDlmMGI0ZmEwZDVjNGMwYjZiNDcyOWY5ZDFjNiJ9fX0", "Water Essence", ChatColor.BLUE));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjEzZjc3MGIzMTI5OWM4ZTg0MDNjZWFkZGNjMTZhNzVkNjc1NDVmY2Y5NzEyMjk2YWNjNWRiOGIxZGMzY2VlOCJ9fX0", "Fire Essence", ChatColor.GOLD));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFhNjI4ZTBlZDIyOWE0ZDc4MmMzMTlkZWJkYjY0YWY5ZWY5MTBkNTJiZjllOTE0NTg1NDRjY2ZkMTYwMmQifX19", "Earth Essence", ChatColor.of("#9f7323")));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGYyNmVhOTNkNWZkMTlhMzgwOGE1ZTU4ODVmYzI5NjEyNjU3ZDgzZGZhYjliZmY1MjcyNzljY2JkNmYxNiJ9fX0", "Wind Essence", ChatColor.WHITE));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I5YjJhNGQ1OTc4MWQxYmVjMmQ4Mjc4ZjIzOTg1ZTc0OWM4ODFiNzJkNzg3NmM5NzllNzFmZGE1YmQzYyJ9fX0", "Ice Essence", ChatColor.AQUA));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFiOWEwYTZkMWI5OTEyNzk0Mjg5ZWNhMWUyMjRlYWU2ZDc2YTdjYjc1MmNhNjg5ZjFiOTkxY2U5NzBhZGVlIn19fQ", "Lightning Essence", ChatColor.LIGHT_PURPLE));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjZTk1ZGQ0ODBmMTdmZWUwNzFmOWU4ZjdlMmU5YzA1NDJhYzcyYWI1ZDJhZTUzY2NlYWQyYmQ3MjM3MGMyNSJ9fX0", "Nature Essence", ChatColor.DARK_GREEN));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMwMmYxNGViNjM3MTcxMTI0ZDdhOWY2ZDA1NzM3OWZiODcxMGEyY2U4ZGNjY2MwZWRkNTI5ZGJmZDk3ZTE4NyJ9fX0", "Sound Essence", ChatColor.WHITE));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2FmMDM5YmVjMWZjMWZiNzUxOTYwOTJiMjZlNjMxZjM3YTg3ZGZmMTQzZmMxODI5Nzc5OGQ0N2M1ZWFhZiJ9fX0", "Light Essence", ChatColor.WHITE));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UxZTVjODFmYjlkNjRiNzQ5OTZmZDE3MTQ4OWRlYWRiYjhjYjc3MmQ1MmNmOGI1NjZlM2JjMTAyMzAxMDQ0In19fQ", "Dark Essence", ChatColor.BLACK));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkwMjUzYzQ5ZTEzY2MyZGUwMDA5MGVlNjU4MDlkYTYxN2M1M2Y1OWUzNWYwOWE3YzZlMzUwMTFkMTlhY2IzZCJ9fX0", "Nether Essence", ChatColor.DARK_RED));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmExNDc0ZDg5MWQyYTY0N2VlMWFhNzliNTg5NDRhNTZlN2I4M2FiZDliN2NjMTM0ZTQyMGMxYjNhN2M0OTk4In19fQ", "End Essence", ChatColor.of("#fff77b")));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjlmZGEzMDkzYzA3M2U0NDgzZDkwYzBiZjM3MjA5NDUxMWU1MTE4MGVkNTFiY2M5ZGFkYzJmZGU1MmY0ZGIzOSJ9fX0", "Alchemy Essence", ChatColor.GREEN));
		itemManager.addItem(generateEssenceItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhjZWI4NjMxYWRkN2NiYjU2NWRjYjcwNWYxMjEyMzQ5Y2NjZDc1NTk2NWM0NmE5MjI4NTJjOWZkOTQ4YjRiYiJ9fX0", "Forbidden Essence", ChatColor.of("#ce137b")));
		essenceDropList.put(EntityType.ALLAY, List.of(new IntStr("wind_essence", 8), new IntStr("sound_essence", 2), new IntStr("light_essence", 2)));
		essenceDropList.put(EntityType.AXOLOTL, List.of(new IntStr("water_essence", 8)));
		essenceDropList.put(EntityType.BAT, List.of(new IntStr("earth_essence", 1), new IntStr("wind_essence", 3), new IntStr("sound_essence", 1)));
		essenceDropList.put(EntityType.CHICKEN, List.of(new IntStr("earth_essence", 2), new IntStr("wind_essence", 1)));
		essenceDropList.put(EntityType.COD, List.of(new IntStr("water_essence", 2)));
		essenceDropList.put(EntityType.COW, List.of(new IntStr("earth_essence", 3)));
		essenceDropList.put(EntityType.DONKEY, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.FOX, List.of(new IntStr("earth_essence", 2)));
		essenceDropList.put(EntityType.FROG, List.of(new IntStr("earth_essence", 4), new IntStr("water_essence", 4)));
		essenceDropList.put(EntityType.GLOW_SQUID, List.of(new IntStr("water_essence", 2)));
		essenceDropList.put(EntityType.HORSE, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.MUSHROOM_COW, List.of(new IntStr("earth_essence", 3), new IntStr("nature_essence", 2)));
		essenceDropList.put(EntityType.MULE, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.PARROT, List.of(new IntStr("earth_essence", 1), new IntStr("wind_essence", 2)));
		essenceDropList.put(EntityType.PIG, List.of(new IntStr("earth_essence", 2)));
		essenceDropList.put(EntityType.PUFFERFISH, List.of(new IntStr("water_essence", 3)));
		essenceDropList.put(EntityType.RABBIT, List.of(new IntStr("earth_essence", 2), new IntStr("wind_essence", 1)));
		essenceDropList.put(EntityType.SALMON, List.of(new IntStr("water_essence", 2)));
		essenceDropList.put(EntityType.SHEEP, List.of(new IntStr("earth_essence", 3)));
		essenceDropList.put(EntityType.SKELETON_HORSE, List.of(new IntStr("forbidden_essence", 1)));
		essenceDropList.put(EntityType.SNOWMAN, List.of(new IntStr("ice_essence", 1)));
		essenceDropList.put(EntityType.SQUID, List.of(new IntStr("water_essence", 2)));
		essenceDropList.put(EntityType.STRIDER, List.of(new IntStr("fire_essence", 5), new IntStr("nether_essence", 1)));
		essenceDropList.put(EntityType.TADPOLE, List.of(new IntStr("water_essence", 2)));
		essenceDropList.put(EntityType.TROPICAL_FISH, List.of(new IntStr("water_essence", 2)));
		essenceDropList.put(EntityType.TURTLE, List.of(new IntStr("water_essence", 3), new IntStr("wind_essence", 3)));
		essenceDropList.put(EntityType.VILLAGER, List.of(new IntStr("earth_essence", 5)));
		essenceDropList.put(EntityType.WANDERING_TRADER, List.of(new IntStr("earth_essence", 5)));

		essenceDropList.put(EntityType.BEE, List.of(new IntStr("earth_essence", 1), new IntStr("wind_essence", 2), new IntStr("nature_essence", 1)));
		essenceDropList.put(EntityType.CAVE_SPIDER, List.of(new IntStr("earth_essence", 3)));
		essenceDropList.put(EntityType.DOLPHIN, List.of(new IntStr("water_essence", 4), new IntStr("sound_essence", 1)));
		essenceDropList.put(EntityType.ENDERMAN, List.of(new IntStr("end_essence", 1)));
		essenceDropList.put(EntityType.GOAT, List.of(new IntStr("earth_essence", 3)));
		essenceDropList.put(EntityType.IRON_GOLEM, List.of(new IntStr("earth_essence", 18)));
		essenceDropList.put(EntityType.LLAMA, List.of(new IntStr("earth_essence", 3), new IntStr("water_essence", 1)));
		essenceDropList.put(EntityType.PANDA, List.of(new IntStr("earth_essence", 3)));
		essenceDropList.put(EntityType.PIGLIN, List.of(new IntStr("nether_essence", 2)));
		essenceDropList.put(EntityType.POLAR_BEAR, List.of(new IntStr("earth_essence", 3), new IntStr("ice_essence", 1)));
		essenceDropList.put(EntityType.SPIDER, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.TRADER_LLAMA, List.of(new IntStr("earth_essence", 3), new IntStr("water_essence", 1)));
		essenceDropList.put(EntityType.WOLF, List.of(new IntStr("earth_essence", 3)));
		essenceDropList.put(EntityType.ZOMBIFIED_PIGLIN, List.of(new IntStr("nether_essence", 2)));

		essenceDropList.put(EntityType.BLAZE, List.of(new IntStr("fire_essence", 4),new IntStr("nether_essence", 4)));
		essenceDropList.put(EntityType.CREEPER, List.of(new IntStr("fire_essence", 1),new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.DROWNED, List.of(new IntStr("water_essence", 4),new IntStr("earth_essence", 2)));
		essenceDropList.put(EntityType.ELDER_GUARDIAN, List.of(new IntStr("water_essence", 6),new IntStr("sound_essence", 3)));
		essenceDropList.put(EntityType.EVOKER, List.of(new IntStr("forbidden_essence", 1),new IntStr("dark_essence", 2)));
		essenceDropList.put(EntityType.GHAST, List.of(new IntStr("fire_essence", 2),new IntStr("nether_essence", 5)));
		essenceDropList.put(EntityType.GUARDIAN, List.of(new IntStr("water_essence", 4)));
		essenceDropList.put(EntityType.HOGLIN, List.of(new IntStr("earth_essence", 3),new IntStr("nether_essence", 3)));
		essenceDropList.put(EntityType.HUSK, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.MAGMA_CUBE, List.of(new IntStr("fire_essence", 4), new IntStr("nether_essence", 2)));
		essenceDropList.put(EntityType.PHANTOM, List.of(new IntStr("dark_essence", 2), new IntStr("wind_essence", 4), new IntStr("sound_essence", 2)));
		essenceDropList.put(EntityType.PIGLIN_BRUTE, List.of(new IntStr("nether_essence", 5)));
		essenceDropList.put(EntityType.PILLAGER, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.RAVAGER, List.of(new IntStr("earth_essence", 8), new IntStr("dark_essence", 2)));
		essenceDropList.put(EntityType.SHULKER, List.of(new IntStr("end_essence", 2)));
		essenceDropList.put(EntityType.SKELETON, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.SLIME, List.of(new IntStr("alchemy_essence", 1)));
		essenceDropList.put(EntityType.STRAY, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.VEX, List.of(new IntStr("wind_essence", 1)));
		essenceDropList.put(EntityType.VINDICATOR, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.WARDEN, List.of(new IntStr("forbidden_essence", 8), new IntStr("dark_essence", 8)));
		essenceDropList.put(EntityType.WITCH, List.of(new IntStr("forbidden_essence", 1),new IntStr("dark_essence", 1)));
		essenceDropList.put(EntityType.WITHER_SKELETON, List.of(new IntStr("nether_essence", 3)));
		essenceDropList.put(EntityType.ZOGLIN, List.of(new IntStr("earth_essence", 4), new IntStr("nether_essence", 2)));
		essenceDropList.put(EntityType.ZOMBIE, List.of(new IntStr("earth_essence", 4)));
		essenceDropList.put(EntityType.ZOMBIE_VILLAGER, List.of(new IntStr("earth_essence", 4)));

		essenceDropList.put(EntityType.ENDER_DRAGON, List.of(new IntStr("wind_essence", 24), new IntStr("light_essence", 24), new IntStr("end_essence", 24)));
		essenceDropList.put(EntityType.WITHER, List.of(new IntStr("fire_essence", 24), new IntStr("dark_essence", 12), new IntStr("forbidden_essence", 6)));
	}

	private Item generateEssenceItem(String skullTexture, String essence, ChatColor color){
		return Item.generateSkull(skullTexture).setDisplayName(color + essence).setUnplacable().setPDC("ItemID", essence.toLowerCase().replace(" ", "_"));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		if(essenceDropList.containsKey(event.getEntityType())){
			for(IntStr intStr : essenceDropList.get(event.getEntityType())) {
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),itemManager.getItem(intStr.string).setItemAmount(intStr.integer));
			}
		}
	}
}
