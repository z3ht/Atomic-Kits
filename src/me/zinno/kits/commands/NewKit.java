/* Breakthrough: 
 * Java gets rid of all private variables after the program gets out of the class, 
 * so you gotta save/retrieve them somewhere like the config. 
 */

package me.zinno.kits.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.zinno.kits.Main;
import net.md_5.bungee.api.ChatColor;

public class NewKit implements Listener, CommandExecutor {
	
	private Main plugin;
	private String name;

	public NewKit(Main pl) {
		plugin = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command");
			return true;
		}Player player = (Player) sender;
		if(!(player.hasPermission("kits.newkit"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission");
			return true;
		}else if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Usage: /newkit <kit-name>");
			return true;
		}
		name = args[0];
		saveKit(player, addKit());
		return true;
	}
	
	private void saveKit(Player player, boolean newKit) {
		Inventory inv = Bukkit.createInventory(null, 36, ChatColor.LIGHT_PURPLE + name);
		if(newKit == false) {
			for(int order = 0; order < 36; order+=1) {
				if(plugin.getConfig().getItemStack(name+"."+Integer.toString(order))==null)continue;
				ItemStack is = plugin.getConfig().getItemStack(name+"."+Integer.toString(order));
				inv.setItem(order, is);
			}
			plugin.getConfig().set(name, null);
			plugin.saveConfig();
		}
		player.openInventory(inv);
	}
	
	private boolean addKit() {
		if(plugin.getConfig().getStringList("kits").equals(null))plugin.getConfig().createSection("kits");
		List<String> list = plugin.getConfig().getStringList("kits");
		if(list.contains(name))return false;
		list.add(name);
		plugin.getConfig().set("kits", list);
		plugin.saveConfig();
		return true;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(plugin.getConfig().getStringList("kits").size()<1) {
			return;
		} name = plugin.getConfig().getStringList("kits").get(plugin.getConfig().getStringList("kits").size()-1);
		if(!(event.getInventory().getName().equals(ChatColor.LIGHT_PURPLE + name))) {
			return;
		}
		boolean empty = true;
		for(int order = 0; order < 36; order+=1) {
			if(event.getInventory().getItem(order)==null)continue;
			plugin.getConfig().set(name+"."+Integer.toString(order), event.getInventory().getItem(order));
			empty = false;
		}
		if(empty == true) {
			plugin.getConfig().set(name, null);
			List<String> kits = plugin.getConfig().getStringList("kits");
			kits.remove(name);
			plugin.getConfig().set("kits", kits);
			plugin.saveConfig();
			event.getPlayer().sendMessage(ChatColor.RED + "Kit not saved. Reason: Empty");
			return;
		}
		int order = 0;
		for(PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
			ConfigurationSection section = plugin.getConfig().createSection(name+".effect."+Integer.toString(order));
			section.set("type", effect.getType().getName());
			section.set("duration", effect.getDuration());
			section.set("level", effect.getAmplifier());
			order+=1;
		}
		plugin.getConfig().set(name+".effect.amount", order);
		plugin.saveConfig();
		event.getPlayer().sendMessage(ChatColor.GREEN + name + " was successfully saved!");
	}
}