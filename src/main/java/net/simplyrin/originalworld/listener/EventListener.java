package net.simplyrin.originalworld.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.simplyrin.originalworld.Main;
import net.simplyrin.threadpool.ThreadPool;

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
public class EventListener implements Listener {

	private Main instance;

	public EventListener(Main instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		this.instance.dispatchCommand("mw load " + event.getPlayer().getUniqueId().toString());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		UUID uniqueId = event.getPlayer().getUniqueId();

		ThreadPool.run(() -> {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}

			this.instance.dispatchCommand("mw unload " + uniqueId.toString());
		});
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if (this.instance.isOnMyWorld(player)) {
			return;
		}

		if (!this.instance.checkEditPermission(player.getWorld(), player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (this.instance.isOnMyWorld(player)) {
			return;
		}

		if (!this.instance.checkEditPermission(player.getWorld(), player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (this.instance.isOnMyWorld(player)) {
			return;
		}

		if (!this.instance.checkEditPermission(player.getWorld(), player)) {
			event.setCancelled(true);
		}
	}

}
