package net.notlord.machines;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class MachineHandler {
	private final Map<String, Inventory> machines = new HashMap<>();



	public void openMachine(String machineID, Player player){
		player.openInventory(machines.get(machineID));
	}
}
