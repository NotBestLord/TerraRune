package net.notlord.magic;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.notlord.Main;
import net.notlord.item.Item;
import net.notlord.utils.IntStr;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SpellHandler implements Listener {
	private final Map<String, Integer> playersCasting = new HashMap<>();
	private final Map<String, Map<String, Integer>> playerCooldowns = new HashMap<>();
	private final SpellManager spellManager;
	private final Main plugin;

	public SpellHandler(Main plugin){
		this.plugin = plugin;
		spellManager = new SpellManager(plugin);

		for(Player player : Bukkit.getOnlinePlayers()){
			playerCooldowns.put(player.getUniqueId().toString(), new HashMap<>());
			playerCooldowns.put(player.getUniqueId().toString(), new HashMap<>());
		}

		plugin.getRunnableHandler().scheduleTickTask(() -> {
			new HashMap<>(playerCooldowns).forEach((UUID, map) -> {
				new HashMap<>(map).forEach((spellId, cooldown) -> {
					cooldown--;
					if(cooldown == 0){
						map.remove(spellId);
					}
					else{
						map.put(spellId, cooldown);
					}
				});
			});
		});
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		playerCooldowns.put(event.getPlayer().getUniqueId().toString(), new HashMap<>());
		playerCooldowns.put(event.getPlayer().getUniqueId().toString(), new HashMap<>());
	}

	@EventHandler
	public void onPlayerCastSpell(PlayerInteractEvent event){
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getItem() == null) return;
		if(playersCasting.containsKey(event.getPlayer().getUniqueId().toString())) return;
		Item item = new Item(event.getItem());
		if(item.hasPDC("spells",PersistentDataType.STRING) && item.hasPDC("current_spell",PersistentDataType.INTEGER)){
			String[] spellArgs = item.<String>getPDC("spells",PersistentDataType.STRING).split(",");
			int spellIndex = item.getPDC("current_spell",PersistentDataType.INTEGER);
			if(spellIndex >= spellArgs.length) return;
			String id = spellArgs[spellIndex];
			Spell spell = spellManager.getSpell(id);
			if(spell == null) return;
			if(playerCooldowns.get(event.getPlayer().getUniqueId().toString()).containsKey(id)){
				event.getPlayer().sendMessage(ChatColor.RED+"Spell is on cooldown for " +
						String.format("%.1f",playerCooldowns.get(event.getPlayer().getUniqueId().toString()).get(id)/20f) + " seconds.");
				return;
			}
			if(spell.castingTime != 0){
				AtomicInteger time = new AtomicInteger();
				int runId = plugin.getRunnableHandler().scheduleLimitedRepeatingTask(() -> {
					time.set(time.get()+1);
					float percent = 50f*(float)time.get()/(float)spell.castingTime;
					StringBuilder msg = new StringBuilder(ChatColor.GREEN + "");
					for(int i=0;i<50;i++){
						if(i == (int) percent){
							msg.append(ChatColor.WHITE);
						}
						msg.append("|");
					}
					event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg.toString()));
				},1,0,spell.castingTime);
				playersCasting.put(event.getPlayer().getUniqueId().toString(), runId);
			}
			if(spell instanceof Spell.ProjectileSpell projectileSpell){
				plugin.getRunnableHandler().scheduleDelayedTask(() -> {
					if(!playersCasting.containsKey(event.getPlayer().getUniqueId().toString()) && spell.castingTime != 0){
						return;
					}
					if (playersCasting.remove(event.getPlayer().getUniqueId().toString()) != null){
						event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
					}
					ArmorStand armorStand = (ArmorStand) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARMOR_STAND);
					armorStand.setSmall(false);
					Vector dir = event.getPlayer().getLocation().getDirection().clone();
					armorStand.setVisible(false);
					registerCooldown(event.getPlayer().getUniqueId().toString(), spell.id, spell.cooldown + projectileSpell.duration);
					plugin.getRunnableHandler().scheduleLimitedRepeatingTask(() -> {
						armorStand.setVelocity(dir);
						projectileSpell.triggerParticleEvent(armorStand);
						for(Entity entity : armorStand.getNearbyEntities(projectileSpell.size, projectileSpell.size, projectileSpell.size)){
							if(!entity.getUniqueId().toString().equals(event.getPlayer().getUniqueId().toString())){
								projectileSpell.triggerHitEvent(entity);
							}
						}
					},1,0,projectileSpell.duration, armorStand::remove);
				}, spell.castingTime);
			}
			else if(spell instanceof Spell.EffectSpell effectSpell){
				Entity target = event.getPlayer();
				if(effectSpell.range != 0){
					RayTraceResult rayTraceResult = event.getPlayer().rayTraceBlocks(effectSpell.range);
					if(rayTraceResult == null) return;
					target = rayTraceResult.getHitEntity();
					if(target == null) return;
				}
				Entity finalTarget = target;
				plugin.getRunnableHandler().scheduleDelayedTask(() -> {
					if(!playersCasting.containsKey(event.getPlayer().getUniqueId().toString()) && spell.castingTime != 0){
						return;
					}
					if (playersCasting.remove(event.getPlayer().getUniqueId().toString()) != null){
						event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
					}
					effectSpell.triggerStartEvent(finalTarget);
					registerCooldown(event.getPlayer().getUniqueId().toString(), spell.id, spell.cooldown + effectSpell.duration);
					plugin.getRunnableHandler().scheduleDelayedTask(() -> effectSpell.triggerEndEvent(finalTarget), effectSpell.duration);
				}, spell.castingTime);
			}
			else if(spell instanceof Spell.AlterationSpell alterationSpell){
				Entity target1 = event.getPlayer();
				RayTraceResult rayTraceResult = event.getPlayer().rayTraceBlocks(alterationSpell.range);
				if(rayTraceResult == null) return;
				Block target2 = rayTraceResult.getHitBlock();
				if(target2 == null) return;
				plugin.getRunnableHandler().scheduleDelayedTask(() -> {
					if(!playersCasting.containsKey(event.getPlayer().getUniqueId().toString()) && spell.castingTime != 0){
						return;
					}
					if (playersCasting.remove(event.getPlayer().getUniqueId().toString()) != null){
						event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
					}
					alterationSpell.triggerStartEvent(target1, target2);
					registerCooldown(event.getPlayer().getUniqueId().toString(), spell.id, spell.cooldown + alterationSpell.duration);
					if(alterationSpell.duration != 0) {
						plugin.getRunnableHandler().scheduleDelayedTask(() -> alterationSpell.triggerEndEvent(target1, target2), alterationSpell.duration);
					}
				}, spell.castingTime);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(playersCasting.containsKey(event.getPlayer().getUniqueId().toString()) && event.getTo() != null &&
				event.getTo().distance(event.getFrom()) > 0.1){
			plugin.getRunnableHandler().cancelRepeatingTask(playersCasting.remove(event.getPlayer().getUniqueId().toString()));
		}
	}

	private void registerCooldown(String uuid, String spellID, int cooldown){
		if(cooldown != 0){
			playerCooldowns.get(uuid).put(spellID, cooldown);
		}
	}
}
