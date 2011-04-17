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

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	public enum Type {
		WORLD, // World inventory, i.e a Chest or a Furnace
		PLAYER; // Player inventory
	};

	/**
	 * The name of the entity World: block name Player: player name
	 */
	private String name;

	/**
	 * Items stored in this inventory
	 */
	private List<Item> items;

	/**
	 * The inventory's type
	 */
	private Type type;

	/**
	 * The inventory's location
	 */
	private Location location;

	public Inventory(Type type) {
		this.type = type;
		items = new ArrayList<Item>();
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public List<Item> getItems() {
		return items;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Location getLocation() {
		return location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

        public boolean notNull() {
                return ((name != null) && (location != null) && (items != null));
        }


	@Override
	public String toString() {
		return String.format("%s (%s) %s %d items", name, type, location.toString(), items.size());
	}

}
