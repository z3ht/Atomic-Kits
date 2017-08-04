package me.zinno.kits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.zinno.kits.Main;
import net.md_5.bungee.api.ChatColor;

public class NewEffect implements CommandExecutor{
	private Main plugin;
	public NewEffect(Main pl) {
		plugin = pl;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command");
			return true;
		}Player player = (Player) sender;
		if(!(player.hasPermission("kits.neweffect"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission");
			return true;
		}else if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Usage: /neweffect <kit-name>");
			return true;
		}else if(!(plugin.getConfig().getStringList("kits").contains(args[0]))) {
			player.sendMessage(ChatColor.RED + "Use: /newkit <kit-name> (to create a kit) before updating the potion effects");
			return true;
		}
		int order = 0;
		plugin.getConfig().set(args[0]+".effect", null);
		for(PotionEffect effect : player.getActivePotionEffects()) {
			ConfigurationSection section = plugin.getConfig().createSection(args[0]+".effect."+Integer.toString(order));
			section.set("type", effect.getType().getName());
			section.set("duration", effect.getDuration());
			section.set("level", effect.getAmplifier());
			order+=1;
		}
		plugin.getConfig().set(args[0]+".effect.amount", order);
		plugin.saveConfig();
		player.sendMessage(ChatColor.GREEN + "Kit effects updated successfully");
		return true;
	}
	
}
