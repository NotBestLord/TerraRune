package net.notlord.commands;

import net.notlord.Main;
import net.notlord.item.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemCommand implements CommandExecutor {
	private final Main main;

	public ItemCommand(Main plugin) {
		this.main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Error");
			return true;
		}
		if (args.length == 0) {
			return true;
		}
		if (args.length == 1 && this.main.getItemManager().getItem(args[0].toLowerCase()) != null) {
			player.getInventory().addItem(this.main.getItemManager().getItem(args[0].toLowerCase()));
		}
		else if(args.length == 2 && args[0].equalsIgnoreCase("skull")){
			player.getInventory().addItem(Item.generateSkull(args[1]));
		}
		return false;
	}
}
