package net.simplyrin.originalworld.command;

import static net.simplyrin.originalworld.WorldType.*;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joor.Reflect;

import multiworld.MultiWorldPlugin;
import multiworld.command.CommandStack;
import multiworld.command.DebugLevel;
import multiworld.data.WorldHandler;
import multiworld.worldgen.WorldGenerator;
import net.simplyrin.originalworld.Main;
import net.simplyrin.originalworld.location.Spawn;

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
public class CommandOriginalWorld implements CommandExecutor {

	private Main instance;

	public CommandOriginalWorld(Main instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			this.instance.sendMessage(sender, "&cこのコマンドはゲーム内からのみ使用できます。");
			return true;
		}

		if (!sender.hasPermission("originalworld.command")) {
			this.instance.sendMessage(sender, "&cあなたはこのコマンドを使用することができません。");
			return true;
		}

		Player player = (Player) sender;

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("create")) {
				if (this.instance.getConfig().getBoolean("Player." + player.getUniqueId().toString() + ".Created")) {
					this.instance.sendMessage(sender, "&cあなたは既にワールドを作成しています！");
					this.instance.sendMessage(sender, "&c'/" + cmd.getName() + " tp' を使用してワールドにテレポートすることができます。");
					return true;
				}

				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("normal")) {
						this.createWorld(player, NORMAL);
						return true;
					}

					if (args[1].equalsIgnoreCase("flat")) {
						this.createWorld(player, FLAT);
						return true;
					}
				}

				this.instance.sendMessage(sender, "&b使用方法: /" + cmd.getName() + " create <type> : ワールドを作成します。");
				this.instance.sendMessage(sender, "&cワールドタイプを指定する必要があります。");
				this.instance.sendMessage(sender, "&cnormal : 通常ワールド");
				this.instance.sendMessage(sender, "&cflat : フラットワールド");
				return true;
			}

			if (args[0].equalsIgnoreCase("tp")) {
				World world = this.instance.getServer().getWorld(player.getUniqueId().toString());

				if (world == null) {
					this.instance.sendMessage(sender, "&cあなたはまだオリジナルワールドを作成していません！");
					this.instance.sendMessage(sender, "&c'/" + cmd.getName() + " create' コマンドを使用してワールドを作成して下さい！");
					return true;
				}

				Spawn spawn = Spawn.fromString(this.instance.getConfig().getString("Player." + player.getUniqueId().toString() + ".Spawn"));

				this.instance.getServer().getScheduler().runTask(this.instance, () -> {
					player.teleport(new Location(world, spawn.getX(), spawn.getY(), spawn.getZ()));
				});
				return true;
			}

			if (args[0].equalsIgnoreCase("add")) {
				if (args.length > 1) {
					OfflinePlayer offlinePlayer = this.instance.getServer().getOfflinePlayer(args[1]);
					if (offlinePlayer == null) {
						this.instance.sendMessage(sender, "&cプレイヤーが見つかりませんでした。");
						return true;
					}

					this.instance.getConfig().set("Player." + player.getUniqueId().toString() + ".Editor."
							+ offlinePlayer.getUniqueId().toString(), true);
					this.instance.sendMessage(sender, "&a" + offlinePlayer.getName() + " にワールドの編集許可を与えました。");
					return true;
				}

				this.instance.sendMessage(sender, "&b使用方法: /" + cmd.getName() + " add <player> : ターゲットプレイヤーに自分のワールドを編集する権利を付与します。");
				return true;
			}

			if (args[0].equalsIgnoreCase("remove")) {
				if (args.length > 1) {
					OfflinePlayer offlinePlayer = this.instance.getServer().getOfflinePlayer(args[1]);
					if (offlinePlayer == null) {
						this.instance.sendMessage(sender, "&cプレイヤーが見つかりませんでした。");
						return true;
					}

					this.instance.getConfig().set("Player." + player.getUniqueId().toString() + ".Editor."
							+ offlinePlayer.getUniqueId().toString(), false);
					this.instance.sendMessage(sender, "&a" + offlinePlayer.getName() + " からワールドの編集許可を剥奪しました。");
					return true;
				}

				this.instance.sendMessage(sender, "&b- /" + cmd.getName() + " remove <player> : ターゲットプレイヤーから自分のワールドを編集する権利を剥奪します。");
				return true;
			}

			if (args[0].equalsIgnoreCase("list")) {
				Set<String> keys = this.instance.getConfig().getConfigurationSection("Player." + player.getUniqueId().toString() + ".Editor").getKeys(false);

				this.instance.sendMessage(sender, "§b編集許可しているプレイヤーリスト");
				for (String uniqueId : keys) {
					if (this.instance.getConfig().getBoolean("Player." + player.getUniqueId().toString() + ".Editor." + uniqueId)) {
						OfflinePlayer offlinePlayer = this.instance.getServer().getOfflinePlayer(UUID.fromString(uniqueId));
						this.instance.sendMessage(sender, "§b" + offlinePlayer.getName() + " (" + offlinePlayer.getUniqueId().toString() + ")");
					}
				}
				return true;
			}
		}

		this.instance.sendMessage(sender, "&b- コマンドリスト -");
		this.instance.sendMessage(sender, "&b- /" + cmd.getName() + " create <type> : ワールドを作成します。");
		this.instance.sendMessage(sender, "&b- /" + cmd.getName() + " tp : 作成したワールドにテレポートします。");
		this.instance.sendMessage(sender, "&b- /" + cmd.getName() + " add <player> : ターゲットプレイヤーに自分のワールドを編集する権利を付与します。");
		this.instance.sendMessage(sender, "&b- /" + cmd.getName() + " remove <player> : ターゲットプレイヤーから自分のワールドを編集する権利を剥奪します。");
		this.instance.sendMessage(sender, "&b- /" + cmd.getName() + " list : 編集を許可したプレイヤーを確認します。");
		return true;
	}

	private void createWorld(Player player, WorldGenerator type) {
		this.instance.sendMessage(player, "&7ワールドを作成しています...。");

		long seed = new Random().nextLong();
		try {
			MultiWorldPlugin plugin = MultiWorldPlugin.getInstance();
			plugin.getDataManager().getWorldManager().makeWorld(player.getUniqueId().toString(), type, seed, "");
			WorldHandler world = Reflect.on(plugin).field("worldHandler").get();
			CommandStack stack = plugin.builder.build(player, DebugLevel.NONE).editStack().setArguments("".split("")).setCommandLabel("").build();
			world.loadWorld(player.getUniqueId().toString(), stack);
		} catch (Exception e) {
			this.instance.sendMessage(player, "&cワールド作成中にエラーが発生しました。");
			e.printStackTrace();
			return;
		}
		this.instance.sendMessage(player, "&aワールドを作成しました！ワールドへテレポートします。");

		this.instance.getConfig().set("Player." + player.getUniqueId().toString() + ".Created", true);

		World world = this.instance.getServer().getWorld(player.getUniqueId().toString());
		Location spawn = world.getSpawnLocation();

		this.instance.getServer().getScheduler().runTask(this.instance, () -> {
			player.setGameMode(GameMode.CREATIVE);
			player.teleport(new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ()));
			Location location = player.getLocation();
			this.instance.getConfig().set("Player." + player.getUniqueId().toString() + ".Spawn", new Spawn(location.getX(), location.getY(), location.getZ()).toString());
		});
	}

}
