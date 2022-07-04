package net.notlord.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemTagHandler implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if(new Item(event.getItemInHand()).hasPDC("unplacable", PersistentDataType.INTEGER)){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event){
		Item item = new Item(event.getItemDrop().getItemStack());
		if(item.hasPDC("holo", PersistentDataType.INTEGER)){
			event.getItemDrop().remove();
		}
	}
	@EventHandler
	public void onItemDrop2(PlayerDeathEvent event){
		event.getDrops().removeIf(itemStack -> new Item(itemStack).hasPDC("holo", PersistentDataType.INTEGER));
	}
}
