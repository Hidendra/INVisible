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

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.griefcraft.minecraft.Inventory;
import com.griefcraft.minecraft.RegionFile;

public class BetaWorldLoader implements ILoader {

	/**
	 * Recurse into the directories, reading all of world files
	 * 
	 * @param folder
	 * @param inventories
	 * @return
	 * @throws IOException
	 */
	private List<Inventory> recurse(File folder, List<Inventory> inventories) throws IOException {
		if (!folder.exists()) {
			return inventories;
		}

		if (!folder.isDirectory()) {
			return inventories;
		}

		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				String path = file.getPath();

				if (path.endsWith("players")) {
					continue;
				}

				inventories = recurse(file, inventories);
			} else {
				String fileName = file.getName();

				if (!fileName.endsWith(".mcr")) {
					continue;
				}

				RegionFile region = new RegionFile(file);

				for (int x = 0; x < 32; x++) {
					for (int z = 0; z < 32; z++) {
						DataInputStream inputStream = region.getChunkDataInputStream(x, z);

						if (inputStream == null) {
							continue;
						}

						List<Inventory> tmp = NBTWorldLoader.readFrom(inputStream, false);
						inventories.addAll(tmp);
						inputStream.close();
					}
				}
			}
		}

		return inventories;
	}

	@Override
	public List<Inventory> load(String world) throws IOException {
		List<Inventory> inventories = new ArrayList<Inventory>();
		File worldFolder = new File(world + "/region/");

		return recurse(worldFolder, inventories);
	}

}
