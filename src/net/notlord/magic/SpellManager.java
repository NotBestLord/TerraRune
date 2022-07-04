package net.notlord.magic;

import net.md_5.bungee.api.ChatColor;
import net.notlord.Main;
import net.notlord.item.Item;
import net.notlord.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Container;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellManager {
	private final Map<String, Spell> spells = new HashMap<>();

	public SpellManager(Main plugin) {
		addSpell(new Spell.ProjectileSpell("mana_ball",0,20,100,
				(entity) -> entity.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,entity.getLocation(),0),40,0.5f,
				(entity)-> {
					if(entity instanceof LivingEntity livingEntity){
						livingEntity.damage(10);
					}
				}));
		GrimoireHandler.addSpellRecipe("mana_ball", List.of(plugin.getItemManager().getItem("water_essence").setItemAmount(32)),
				ChatColor.AQUA+"Mana Ball", 1,1);
		addSpell(new Spell.EffectSpell("mana_sword",0,20, 80,
				(entity) -> {
					if(entity instanceof Player player){
						player.getInventory().addItem(plugin.getItemManager().getItem("holo_sword"));
					}
				},0, 200,
				(entity)-> {
					if(entity instanceof Player player){
						ItemUtils.removeItemIDFromInv(player.getInventory(), "holo_sword");
					}
				}));
		GrimoireHandler.addSpellRecipe("mana_sword", List.of(
				        plugin.getItemManager().getItem("earth_essence").setItemAmount(32),
						plugin.getItemManager().getItem("fire_essence").setItemAmount(32)),
				ChatColor.WHITE+"Mana Sword", 2,1);
		addSpell(new Spell.AlterationSpell("smelt",0,40, 120,
				(entity, block) -> {
					ItemStack item = Map.of(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT,2),
							Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT,2)).get(block.getType());
					if(item != null){
						block.setType(Material.AIR);
						block.getWorld().dropItemNaturally(block.getLocation(), item);
					}
				},16, 0,null));
		GrimoireHandler.addSpellRecipe("smelt", List.of(plugin.getItemManager().getItem("fire_essence").setItemAmount(32)),
				ChatColor.RED+"Smelt", 1,1);

	}

	public void addSpell(Spell spell){
		spells.put(spell.id, spell);
	}

	public Spell getSpell(String key){
		return spells.get(key);
	}
}
