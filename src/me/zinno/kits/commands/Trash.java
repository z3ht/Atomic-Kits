package me.zinno.kits.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Trash implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command");
			return true;
		}Player player = (Player) sender;
		if(!(player.hasPermission("kits.trash"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission");
			return true;
		}else if(args.length != 0 || args == null) {
			player.sendMessage(ChatColor.RED + "Usage: /trash");
			return true;
		}
		player.openInventory(Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Trash"));
		player.sendMessage(ChatColor.GREEN + "Trash Removed! Have a nice day!");
		return true;
	}

}
