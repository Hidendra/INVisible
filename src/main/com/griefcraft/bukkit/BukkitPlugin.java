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

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.INVisible;
import com.nijikokun.bukkit.Permissions.Permissions;

public class BukkitPlugin extends JavaPlugin {

	/**
	 * The INVisible object
	 */
	private INVisible invisible;
	
	/**
	 * The permissions plugin
	 */
	private Permissions permissions;

	@Override
	public void onDisable() {
		invisible = null;
	}

	@Override
	public void onEnable() {
		checkPermissions();

		invisible = new INVisible(null);

		getCommand("inv").setExecutor(new BukkitExecutor(this));

		System.out.println("INVisible v" + INVisible.VERSION + " is ready!");
	}
	
	/**
	 * Check for a Permissions plugin
	 */
	private void checkPermissions() {
		Plugin permissionsPlugin = getServer().getPluginManager().getPlugin("Permissions");

		if (permissionsPlugin != null) {
			System.out.println("Using Nijikokun's permissions plugin for permissions");

			if (!permissionsPlugin.isEnabled()) {
				getServer().getPluginManager().enablePlugin(permissionsPlugin);
			}

			permissions = (Permissions) permissionsPlugin;
		}
	}
	
	/**
	 * @return true if Permissions is enabled
	 */
	public boolean usePermissions() {
		return permissions != null;
	}

	/**
	 * @return the INVisible object
	 */
	public INVisible getINVisible() {
		return invisible;
	}

}
