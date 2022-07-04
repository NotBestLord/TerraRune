package net.notlord.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ItemAbilityHandler implements Listener {
	private static final Map<Class<?>, Map<String, Consumer<Object>>> events = new HashMap<>();

	public static void addEvent(Class<?> eventClass, String key, Consumer<Object> event){
		if(!events.containsKey(eventClass)){
			events.put(eventClass, new HashMap<>());
		}
		events.get(eventClass).put(key.toLowerCase(),event);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		if(event.getItem() != null) {
			String id = new Item(event.getItem()).getPDC("ItemID", PersistentDataType.STRING);
			if (id != null && events.get(PlayerInteractEvent.class).containsKey(id.toLowerCase())) {
				events.get(PlayerInteractEvent.class).get(id.toLowerCase()).accept(event);
			}
		}
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractAtEntityEvent event){
		if(event.getPlayer().getEquipment() == null) return;
		String id = new Item(event.getPlayer().getEquipment().getItem(event.getHand())).getPDC("ItemID", PersistentDataType.STRING);
		if (id != null && events.get(PlayerInteractAtEntityEvent.class).containsKey(id.toLowerCase())) {
			events.get(PlayerInteractAtEntityEvent.class).get(id.toLowerCase()).accept(event);
		}
	}
}
