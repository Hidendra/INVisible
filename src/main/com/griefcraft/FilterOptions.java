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

package com.griefcraft;

import java.util.ArrayList;
import java.util.List;

import com.griefcraft.minecraft.Inventory.Type;

public class FilterOptions {

	/**
	 * List of items to search for
	 */
	public static List<Integer> ITEMS = new ArrayList<Integer>();

	/**
	 * The inventory type to filter by
	 */
	public static Type TYPE = null;

	/**
	 * The name of the entity to filter by
	 */
	public static String NAME;

	/**
	 * The current page number
	 */
	public static int PAGE = 1;

	/**
	 * The number of protections to show per page
	 */
	public static int PER_PAGE = 5;

}
