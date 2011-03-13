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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.griefcraft.minecraft.Inventory;
import com.griefcraft.minecraft.Inventory.Type;
import com.griefcraft.minecraft.Item;
import com.griefcraft.minecraft.Location;
import com.griefcraft.minecraft.Tag;

public class NBTWorldLoader {

	/**
	 * Read a list of inventories from a World NBT input stream
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static List<Inventory> readFrom(InputStream inputStream) throws IOException {
		return readFrom(inputStream, true);
	}

	/**
	 * Read a list of inventories from a World NBT input stream
	 * 
	 * @param inputStream
	 * @param isGZipped
	 * @return
	 * @throws IOException
	 */
	public static List<Inventory> readFrom(InputStream inputStream, boolean isGZipped) throws IOException {
		List<Inventory> inventories = new ArrayList<Inventory>();
		Tag root = Tag.readFrom(inputStream, isGZipped);

		if (root == null) {
			return null;
		}

		// root/Level
		Tag level = root.findTagByName("Level");

		if (level == null) {
			return null;
		}

		// root/Level/TileEntities
		Tag tileEntities = root.findTagByName("TileEntities");

		if (tileEntities == null) {
			return null;
		}

		// root/Level/TileEntities/*
		Tag[] entities = (Tag[]) tileEntities.getValue();

		// root/Level/TileEntities/?
		for (Tag entity : entities) {
			Inventory inventory = new Inventory(Type.WORLD);

			// root/Level/TileEntities/?/Items
			Tag itemsRoot = entity.findTagByName("Items");

			if (itemsRoot == null) {
				continue;
			}

			String block = (String) Tag.findTagByType(entity, "id", Tag.Type.TAG_String).getValue();
			int x = (Integer) entity.findTagByName("x").getValue();
			int y = (Integer) entity.findTagByName("y").getValue();
			int z = (Integer) entity.findTagByName("z").getValue();

			// root/Level/TileEntities/?/Items/*
			Tag[] items = (Tag[]) itemsRoot.getValue();

			if (items == null) {
				continue;
			}

			for (Tag child : items) {
				if (child == null) {
					continue;
				}

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

				inventory.setName(block);
				inventory.setLocation(location);
				inventory.addItem(item);
			}

			inventories.add(inventory);
		}

		return inventories;
	}

}
