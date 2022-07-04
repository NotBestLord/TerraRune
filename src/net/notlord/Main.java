package net.notlord;

import net.notlord.commands.InfoCommand;
import net.notlord.commands.ItemCommand;
import net.notlord.crafting.CraftingTable;
import net.notlord.crafting.RecipeManager;
import net.notlord.item.Item;
import net.notlord.item.ItemAbilityHandler;
import net.notlord.item.ItemManager;
import net.notlord.item.ItemTagHandler;
import net.notlord.magic.ElvenTradeHandler;
import net.notlord.magic.EssenceHandler;
import net.notlord.magic.GrimoireHandler;
import net.notlord.magic.SpellHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private RunnableHandler runnableHandler;
	private ItemManager itemManager;

	@Override
	public void onEnable() {
		Item.init(this);
		runnableHandler = new RunnableHandler(this);
		itemManager = new ItemManager(this);
		getCommand("gitem").setExecutor(new ItemCommand(this));
		getCommand("info").setExecutor(new InfoCommand(this));
		getServer().getPluginManager().registerEvents(new ItemTagHandler(),this);
		getServer().getPluginManager().registerEvents(new ItemAbilityHandler(),this);
		getServer().getPluginManager().registerEvents(runnableHandler,this);
		getServer().getPluginManager().registerEvents(new EssenceHandler(itemManager),this);
		getServer().getPluginManager().registerEvents(new SpellHandler(this),this);
		getServer().getPluginManager().registerEvents(new GrimoireHandler(this),this);
		getServer().getPluginManager().registerEvents(new ElvenTradeHandler(this),this);


		RecipeManager.init(this);
		getServer().getPluginManager().registerEvents(new CraftingTable(this),this);
	}

	public RunnableHandler getRunnableHandler() {
		return runnableHandler;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}
}
