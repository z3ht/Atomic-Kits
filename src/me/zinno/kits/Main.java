package me.zinno.kits;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.zinno.kits.commands.DelKit;
import me.zinno.kits.commands.Kit;
import me.zinno.kits.commands.NewEffect;
import me.zinno.kits.commands.NewKit;
import me.zinno.kits.commands.Trash;


public class Main extends JavaPlugin {
	public void onEnable() {
		registerCommands();
		registerConfig();
		registerEvents();
	}

	public void onDisable() {
	}
	
	public void registerCommands() {
		getCommand("kit").setExecutor(new Kit(this));
		getCommand("newkit").setExecutor(new NewKit(this));
		getCommand("neweffect").setExecutor(new NewEffect(this));
		getCommand("delkit").setExecutor(new DelKit(this));
		getCommand("trash").setExecutor(new Trash());
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new NewKit(this), this);
	}
	
	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}