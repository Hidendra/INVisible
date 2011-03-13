/**
 * This file is part of INVisible (https://github.com/Hidendra/INVisible)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.griefcraft.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nijikokun.bukkit.Permissions.Permissions;

public class BukkitExecutor implements CommandExecutor {

	private BukkitPlugin plugin;

	public BukkitExecutor(BukkitPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String commandLine = "";
		
		// check permissions
		if(sender instanceof Player) {
			Player player = (Player) sender;
			boolean allowed = true;
			
			if(plugin.usePermissions()) {
				if(!Permissions.Security.permission(player, "invisible.use")) {
					allowed = false;
				}
			} else {
				if(!player.isOp()) {
					allowed = false;
				}
			}
			
			if(!allowed) {
				player.sendMessage("Permission denied (INVisible)");
				return true;
			}
		}

		// combine the args
		for (String arg : args) {
			commandLine += arg + " ";
		}

		commandLine = commandLine.trim();

		// check if there are no args (i.e send help)
		if (commandLine.isEmpty()) {
			commandLine = "help";
		}

		// set the sender in INVisible and then process
		plugin.getINVisible().setCommandSender(sender);
		plugin.getINVisible().processCommand(commandLine);

		return true;
	}

}
