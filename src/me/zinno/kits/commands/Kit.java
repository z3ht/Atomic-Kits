package me.zinno.kits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.zinno.kits.Main;
import net.md_5.bungee.api.ChatColor;

public class Kit implements CommandExecutor {
	private Main plugin;
	
	public Kit(Main pl) {
		plugin = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can issue this command");
			return true;
		}Player player = (Player) sender;
		if(args == null || args.length > 1) {
			player.sendMessage(ChatColor.RED + "Usage: /kit <kit-name> or /kit");
			return true;
		} else if(args.length == 0) {
			player.sendMessage(ChatColor.BLUE + "Available Kits:");
			boolean hasKits = false;
			for(String s : plugin.getConfig().getStringList("kits")) {
				if(player.hasPermission("kits."+s)) {
					player.sendMessage(ChatColor.AQUA + "  - " + s);
					hasKits = true;
				}
			}
			if(hasKits == false)player.sendMessage(ChatColor.RED + "Whoops... you don't have any kits");
			return true;
		} else if(!(plugin.getConfig().getStringList("kits").contains(args[0]))) {
			player.sendMessage(ChatColor.RED + "Error: Kit not found");
			return true;
		} else if(!(player.hasPermission("kits." + args[0]))) {
			player.sendMessage(ChatColor.RED + "You do not have access to kit: " + ChatColor.BOLD + args[0]);
			return true;
		}
		for(int order = 0; order < 36; order+=1) {
			int playerOrder = (order+9)%36;
			if(plugin.getConfig().getItemStack(args[0]+"."+Integer.toString(order))==null)continue;
			ItemStack is = plugin.getConfig().getItemStack(args[0]+"."+Integer.toString(order));
			if(player.getInventory().getItem(playerOrder)==null) {
				player.getInventory().setItem(playerOrder, is);
				continue;
			}player.getInventory().addItem(is);
		}
		
		if(plugin.getConfig().get(args[0]+".effect") == null) {
			player.sendMessage(ChatColor.GREEN + "Enjoy kit " + args[0] + "!");
			return true;
		}
		for(int order = 0; order < plugin.getConfig().getInt(args[0]+".effect.amount"); order+=1) {
			ConfigurationSection section = plugin.getConfig().getConfigurationSection(args[0]+".effect."+Integer.toString(order));
			String type = section.getString("type");
			int duration = section.getInt("duration");
			int level = section.getInt("level");
			PotionEffect effect = new PotionEffect(PotionEffectType.getByName(type), duration, level);
			player.addPotionEffect(effect);
		}
		player.sendMessage(ChatColor.GREEN + "Enjoy kit " + args[0] + "!");
		return true;
	}
}
