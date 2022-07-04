package net.notlord.crafting;

import net.notlord.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeManager {

	public static void init(Main plugin){
		ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new NamespacedKey(plugin,"lightning_essence"),
				plugin.getItemManager().getItem("lightning_essence"));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("fire_essence").setItemAmount(4)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("water_essence").setItemAmount(4)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("wind_essence").setItemAmount(4)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("wind_essence").setItemAmount(4)));
		plugin.getServer().addRecipe(shapelessRecipe);

		shapelessRecipe = new ShapelessRecipe(new NamespacedKey(plugin,"enchanted_golden_apple"),
				new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(Material.APPLE, 32)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_BLOCK, 48)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_BLOCK, 48)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_BLOCK, 48)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_BLOCK, 48)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("alchemy_essence").setItemAmount(32)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("alchemy_essence").setItemAmount(32)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("alchemy_essence").setItemAmount(32)));
		shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("alchemy_essence").setItemAmount(32)));
		plugin.getServer().addRecipe(shapelessRecipe);

		ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "grimoire_smelt"), plugin.getItemManager().getItem("grimoire_smelt"));
		shapedRecipe.shape(" a ","aba"," a ");
		shapedRecipe.setIngredient('a', new RecipeChoice.ExactChoice(plugin.getItemManager().getItem("fire_essence").setItemAmount(4)));
		shapedRecipe.setIngredient('b', new RecipeChoice.ExactChoice(new ItemStack(Material.BOOK, 4)));
		plugin.getServer().addRecipe(shapedRecipe);

	}
}
