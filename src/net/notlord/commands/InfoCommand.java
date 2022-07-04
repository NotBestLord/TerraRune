package net.notlord.commands;

import net.notlord.Main;
import net.notlord.item.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements CommandExecutor {
	private final Main main;

	public InfoCommand(Main plugin) {
		this.main = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Error");
			return true;
		}
		if(player.getEquipment().getItemInMainHand() != null){
			player.sendMessage(player.getEquipment().getItemInMainHand().toString());
		}
		return false;
	}
}
