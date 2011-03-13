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

package com.griefcraft.minecraft.loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.griefcraft.minecraft.Inventory;
import com.griefcraft.minecraft.Inventory.Type;
import com.griefcraft.minecraft.Item;
import com.griefcraft.minecraft.Location;
import com.griefcraft.minecraft.Tag;

public class PlayerLoader implements ILoader {

	/**
	 * Recurse into each folder in the world folder searching for world files
	 * 
	 * @param folder
	 * @param inventories
	 * @return
	 * @throws IOException
	 */
	public Inventory loadFrom(InputStream inputStream, String name) throws IOException {
		Inventory inventory = new Inventory(Type.PLAYER);
		Tag root = Tag.readFrom(inputStream);

		if (root == null) {
			return null;
		}

		Tag pos = root.findTagByName("Pos");

		if (pos == null) {
			return null;
		}

		Tag[] loc = (Tag[]) pos.getValue();
		int x = (int) Math.round((Double) loc[0].getValue());
		int y = (int) Math.round((Double) loc[1].getValue());
		int z = (int) Math.round((Double) loc[2].getValue());

		// root/inventory/
		Tag inventoryRoot = root.findTagByName("Inventory");

		if (inventoryRoot == null) {
			return null;
		}

		// root/inventory/*
		Tag[] inventoryChildren = (Tag[]) inventoryRoot.getValue();

		if (inventoryChildren == null) {
			return null;
		}

		for (Tag child : inventoryChildren) {
			// root/Inventory/?/val
			int count = (Byte) child.findTagByName("Count").getValue();
			int slot = (Byte) child.findTagByName("Slot").getValue();
			int damage = (Short) child.findTagByName("Damage").getValue();
			int id = (Short) child.findTagByName("id").getValue();

			Item item = new Item();
			item.setSlot(slot);
			item.setId(id);
			item.setAmount(count);
			item.setDamage(damage);

			Location location = new Location();
			location.setX(x);
			location.setY(y);
			location.setZ(z);

			inventory.setName(name);
			inventory.setLocation(location);
			inventory.addItem(item);
		}

		return inventory;
	}

	@Override
	public List<Inventory> load(String world) throws IOException {
		List<Inventory> inventories = new ArrayList<Inventory>();
		File folder = new File(world + "/players/");

		if (!folder.exists()) {
			return inventories;
		}

		if (!folder.isDirectory()) {
			return inventories;
		}

		File[] players = folder.listFiles();

		for (File playerFile : players) {
			String fileName = playerFile.getName();

			if (!fileName.endsWith(".dat")) {
				continue;
			}

			try {

				InputStream inputStream = new FileInputStream(playerFile);
				Inventory tmp = loadFrom(inputStream, fileName.substring(0, fileName.length() - ".dat".length()));
				inputStream.close();

				if (tmp != null) {
					inventories.add(tmp);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return inventories;
	}

}
