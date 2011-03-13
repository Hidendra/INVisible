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

package com.griefcraft.minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.griefcraft.minecraft.Inventory.Type;
import com.griefcraft.minecraft.loaders.AlphaWorldLoader;
import com.griefcraft.minecraft.loaders.BetaWorldLoader;
import com.griefcraft.minecraft.loaders.ILoader;
import com.griefcraft.minecraft.loaders.PlayerLoader;

public class World {

	public enum ChunkType {
		ALPHA, BETA;
	};

	/**
	 * The world name (aka folder name)
	 */
	private String name;

	/**
	 * The list of inventories in the world
	 */
	private List<Inventory> inventories;

	/**
	 * The chunk type this world uses
	 */
	private ChunkType chunkType;

	public World(String name) {
		this.name = name;
		inventories = new ArrayList<Inventory>();
		findChunkType();
	}

	/**
	 * Get the world chunk type version
	 * 
	 * @return
	 */
	public ChunkType getChunkType() {
		return chunkType;
	}

	/**
	 * Determine the chunk type
	 */
	private void findChunkType() {
		File tmp = new File(name + "/region");

		if (tmp.exists() && tmp.isDirectory()) {
			chunkType = ChunkType.BETA;
		} else {
			chunkType = ChunkType.ALPHA;
		}
	}

	/**
	 * @return the world name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the loaded inventories
	 */
	public List<Inventory> getInventories() {
		return inventories;
	}

	/**
	 * Return the count of a specific type in the inventory list
	 * 
	 * @param type
	 * @return
	 */
	public int sizeof(Type type) {
		int count = 0;

		for (Inventory inventory : inventories) {
			if (inventory.getType() == type) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Return the count of inventories
	 * 
	 * @return
	 */
	public int sizeof() {
		return inventories.size();
	}

	/**
	 * Clear all inventories of specified type
	 * 
	 * @param type
	 */
	public void clear(Type type) {
		Iterator<Inventory> iterator = inventories.iterator();

		while (iterator.hasNext()) {
			Inventory inventory = iterator.next();

			if (inventory.getType() == type) {
				iterator.remove();
			}
		}
	}

	/**
	 * Load all inventories of the specified type
	 * 
	 * @param type
	 * @return
	 */
	public boolean load(Type type) {
		boolean success = false;
		ILoader loader = null;

		// clear out inventories of the same type first
		clear(type);

		switch (type) {

		case PLAYER:
			loader = new PlayerLoader();
			break;

		case WORLD:
			if (chunkType == ChunkType.BETA) {
				loader = new BetaWorldLoader();
			} else if (chunkType == ChunkType.ALPHA) {
				loader = new AlphaWorldLoader();
			}

			break;
		}

		try {
			List<Inventory> tmp = loader.load(name);

			inventories.addAll(tmp);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Load all inventories in the world
	 * 
	 * @return
	 */
	public boolean loadAll() {
		boolean success = false;

		for (Type type : Type.values()) {
			success = load(type);
		}

		return success;
	}

	/**
	 * Check to ensure the world is valid
	 * 
	 * @return
	 */
	public boolean isValid() {
		File tmp = new File(name);
		return tmp.exists() && tmp.isDirectory();
	}

}
