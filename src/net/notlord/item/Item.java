package net.notlord.item;

import com.google.common.collect.MultimapBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class Item extends ItemStack {

	private static Plugin plugin;

	public static void init(Plugin main){
		plugin = main;
	}

	public Item(Material type) {
		super(type);
		setItemMeta(getItemMeta());
	}

	public Item(Material type, int amount) {
		super(type, amount);
		setItemMeta(getItemMeta());
	}

	public Item(ItemStack stack) throws IllegalArgumentException {
		super(stack);
		setItemMeta(getItemMeta());
	}

	public Item setItemAmount(int amount){
		setAmount(amount);
		return this;
	}

	public Item setLore(List<String> lore){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.setLore(lore);
			setItemMeta(meta);
		}
		return this;
	}

	public Item setDisplayName(String displayName){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.setDisplayName(displayName);
			setItemMeta(meta);
		}
		return this;
	}

	public Item addItemFlag(ItemFlag flag){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.addItemFlags(flag);
			setItemMeta(meta);
		}
		return this;
	}

	public Item removeItemFlag(ItemFlag flag){
		if(getItemMeta() != null && getItemMeta().hasItemFlag(flag)) {
			ItemMeta meta = getItemMeta();
			meta.removeItemFlags(flag);
			setItemMeta(meta);
		}
		return this;
	}

	/**
	 * @param enchantment
	 * enchant type
	 * @param lvl
	 * level of enchant
	 * @param b
	 * does follow item enchant rules
	 * @return
	 * this
	 */
	public Item addEnchant(Enchantment enchantment, int lvl, boolean b){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.addEnchant(enchantment, lvl, b);
			setItemMeta(meta);
		}
		return this;
	}

	public boolean hasEnchant(Enchantment enchantment){
		if(getItemMeta() != null) {
			return getItemMeta().hasEnchant(enchantment);
		}
		return false;
	}

	public Item removeEnchant(Enchantment enchantment){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.removeEnchant(enchantment);
			setItemMeta(meta);
		}
		return this;
	}

	public Item setUnbreakable(boolean unbreakable){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.setUnbreakable(unbreakable);
			setItemMeta(meta);
		}
		return this;
	}

	public Item setPDC(String key, Object o){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), ItemUtils.getPDTOfObj(o), o);
			setItemMeta(meta);
		}
		return this;
	}

	public boolean hasPDC(String key, PersistentDataType type){
		if(getItemMeta() != null) {
			return getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, key), type);
		}
		return false;
	}

	public <T> T getPDC(String key, PersistentDataType type){
		if(getItemMeta() != null && getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, key), type)) {
			return (T) getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), type);
		}
		return null;
	}

	public Item removePDC(String key){
		if(getItemMeta() != null) {
			ItemMeta meta = getItemMeta();
			meta.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
			setItemMeta(meta);
		}
		return this;
	}

	public Item addAttributeMod(Attribute attribute, AttributeModifier mod){
		if(getItemMeta() != null){
			ItemMeta meta = getItemMeta();
			meta.addAttributeModifier(attribute, mod);
			setItemMeta(meta);
		}
		return this;
	}
	public Item removeAttributeMod(Attribute attribute){
		if(getItemMeta() != null){
			ItemMeta meta = getItemMeta();
			if(!getItemMeta().hasAttributeModifiers()){
				meta.setAttributeModifiers(MultimapBuilder.hashKeys().linkedListValues().build());
			}
			meta.removeAttributeModifier(attribute);
			setItemMeta(meta);
		}
		return this;
	}

	public Item removeAttributeMod(EquipmentSlot slot){
		if(getItemMeta() != null && getItemMeta().hasAttributeModifiers()){
			ItemMeta meta = getItemMeta();
			meta.removeAttributeModifier(slot);
			setItemMeta(meta);
		}
		return this;
	}

	public Item setUnplacable(){
		return setPDC("unplacable", 0);
	}
	public Item setHolo(){
		return setPDC("holo", 0);
	}

	public boolean doesMatch(Item other){
		if(getItemMeta() != null && other.getItemMeta() != null){
			if(hasPDC("ItemID", PersistentDataType.STRING) && other.hasPDC("ItemID", PersistentDataType.STRING)){
				return getPDC("ItemID", PersistentDataType.STRING).equals(other.getPDC("ItemID", PersistentDataType.STRING)) && getAmount() >= other.getAmount();
			}
			else if(!hasPDC("ItemID", PersistentDataType.STRING) && !other.hasPDC("ItemID", PersistentDataType.STRING)){
				return getType() == other.getType() && getAmount() >= other.getAmount();
			}
		}
		else if(getItemMeta() == null && other.getItemMeta() == null){
			return getType() == other.getType() && getAmount() >= other.getAmount();
		}
		return false;
	}

	public String getItemIDOrMaterial(){
		return hasPDC("ItemID", PersistentDataType.STRING) ? this.<String>getPDC("ItemID", PersistentDataType.STRING).toLowerCase() : getType().name().toLowerCase();
	}

	public static Item generateSkull(String value){
		UUID id = UUID.nameUUIDFromBytes(value.getBytes());
		int less = (int)id.getLeastSignificantBits();
		int most = (int)id.getMostSignificantBits();
		Item item = new Item(Material.PLAYER_HEAD);
		item.setItemMeta(Bukkit.getUnsafe().modifyItemStack(new ItemStack(Material.PLAYER_HEAD), "{SkullOwner:{Id:[I;" + less * most + "," + (less >> 23) + "," + most / less + "," + most * 8731 + "],Properties:{textures:[{Value:\"" + value + "\"}]}}}").getItemMeta());
		return item;
	}

}
