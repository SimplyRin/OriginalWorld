package net.simplyrin.originalworld;

import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.simplyrin.originalworld.command.CommandOriginalWorld;
import net.simplyrin.originalworld.listener.EventListener;

/**
 * Created by SimplyRin on 2018/11/11.
 *
 * Copyright (c) 2018 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		if (!this.getServer().getPluginManager().isPluginEnabled("MultiWorld")) {
			this.getLogger().info("MultiWorld が見つかりませんでした。");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		this.saveDefaultConfig();

		this.getCommand("originalworld").setExecutor(new CommandOriginalWorld(this));
		this.getCommand("ow").setExecutor(new CommandOriginalWorld(this));

		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

		new Metrics(this);
	}

	@Override
	public void onDisable() {
		this.saveConfig();
	}

	/**
	 * @return プレイヤーが対象ワールドで編集権利を持っているかどうか
	 */
	public boolean checkEditPermission(World world, Player player) {
		try {
			UUID.fromString(world.getName());
		} catch (Exception e) {
			return false;
		}

		return this.getConfig().getBoolean("Player." + world.getName() + ".Editor." + player.getUniqueId().toString());
	}

	public boolean isOnMyWorld(Player player) {
		World world = player.getWorld();

		return world.getName().equals(player.getUniqueId().toString());
	}

	public void dispatchCommand(String command) {
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), command);
	}

	public void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Prefix") + message));
	}

	public void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Prefix") + message));
	}

}
