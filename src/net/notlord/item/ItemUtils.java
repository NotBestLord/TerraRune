package net.notlord.item;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtils {

	public static PersistentDataType getPDTOfObj(Object o){

		if(o instanceof String){
			return PersistentDataType.STRING;
		}
		if(o instanceof Integer){
			return PersistentDataType.INTEGER;
		}
		if(o instanceof Float){
			return PersistentDataType.FLOAT;
		}
		if(o instanceof Double){
			return PersistentDataType.DOUBLE;
		}
		if(o instanceof Integer[]){
			return PersistentDataType.INTEGER_ARRAY;
		}
		if(o instanceof Byte){
			return PersistentDataType.BYTE;
		}
		return null;
	}

	public static void removeItemIDFromInv(Inventory inventory, String ItemID){
		ItemStack[] contents = inventory.getContents();
		for(ItemStack itemStack : contents){
			if(itemStack != null && new Item(itemStack).hasPDC("ItemID", PersistentDataType.STRING) &&
					new Item(itemStack).getPDC("ItemID", PersistentDataType.STRING).equals(ItemID)){
				inventory.remove(itemStack);
			}
		}
	}
}
