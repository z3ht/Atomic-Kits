package me.zinno.kits.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.zinno.kits.Main;
import net.md_5.bungee.api.ChatColor;

public class DelKit implements CommandExecutor {
	private Main plugin;
	
	public DelKit(Main pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(plugin.getConfig().getStringList("kits").equals(null))plugin.getConfig().createSection("kits");
		else if(args == null || args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /delkit <kit-name> or /delkit");
			return true;
		}
		if(args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "All Available Kits:");
			for(String s : plugin.getConfig().getStringList("kits")) {
				sender.sendMessage(ChatColor.AQUA + "  - " + s);
			}
		}else if(!(plugin.getConfig().getStringList("kits").contains(args[0]))) {
			sender.sendMessage(ChatColor.RED + "That kit does not exist!");
			return true;
		}
		plugin.getConfig().set(args[0], null);
		List<String> list = plugin.getConfig().getStringList("kits");
		list.remove(args[0]);
		plugin.getConfig().set("kits", list);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.GREEN + "Succesfully deleted kit: " + args[0]);
		return true;
	}
}
